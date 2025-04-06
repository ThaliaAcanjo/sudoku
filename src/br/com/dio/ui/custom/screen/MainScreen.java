package br.com.dio.ui.custom.screen;

import static br.com.dio.service.EventEnum.CLEAR_SPACE;
// import static javax.swing.JOptionPane.showOptionDialog;
import static java.util.Objects.nonNull;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.com.dio.model.Generator;
import br.com.dio.model.Space;
import br.com.dio.service.BoardService;
import br.com.dio.service.NotifierService;
import br.com.dio.ui.custom.button.CheckGameStatusButton;
import br.com.dio.ui.custom.button.FinishGameButton;
import br.com.dio.ui.custom.button.NewGameButton;
import br.com.dio.ui.custom.button.ResetButton;
import br.com.dio.ui.custom.combo.LevelCombo;
import br.com.dio.ui.custom.frame.MainFrame;
import br.com.dio.ui.custom.input.NumberText;
import br.com.dio.ui.custom.panel.MainPanel;
import br.com.dio.ui.custom.panel.SudokuSector;

public class MainScreen {
    private final static Dimension dimension = new Dimension(600, 600);
    private  BoardService boardService;
    private final NotifierService notifierService;

    private JButton checkGameStatusButton;
    private JButton finishGameButton;
    private JButton resetButton;
    private JButton newGameButton;
    private JPanel mainPanel;
    private MainFrame mainFrame;
    private JComboBox levelComboBox;
    private Integer level;


    public MainScreen(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();        
        mainPanel = new MainPanel(dimension);
        mainFrame = new MainFrame(dimension, mainPanel);
        this.level = 0;
    }

    public void buildMainScreen() {            
        addSpacesToMainPanel();
        addLevelComboBox(mainPanel);
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        addNewGameButton(mainPanel);
                
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void addSpacesToMainPanel() {
        for (int row = 0; row < boardService.getSpaces().size(); row+=3) {
            var endRow = row + 2;
            for (int col = 0; col < boardService.getSpaces().get(row).size(); col+=3) {
                var endCol = col + 2;
                var spaces = getSpacesFromSector(boardService.getSpaces(), col, endCol, row, endRow);
                mainPanel.add(generatePanel(spaces), null);
            }
        }
    }

    private List<Space> getSpacesFromSector(List<List<Space>> spaces, int startCol, int endCol, int startRow, int endRow) {
        
        List<Space> spaceSector = new ArrayList<>();
        for (int r = startRow; r <= endRow; r++) {
            for (int c = startCol; c <= endCol; c++) {
                spaceSector.add(spaces.get(c).get(r));
            }
        }
        return spaceSector;
    }
    
    private JPanel generatePanel(final List<Space> spaces) {
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(field -> notifierService.subscriber(CLEAR_SPACE, field));
        return new SudokuSector(fields);
    }   
    
    private void addFinishGameButton(final JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e -> {
            if (boardService.isFinish()){
                JOptionPane.showMessageDialog(null, "O jogo foi concluído!");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            }
            else{
                JOptionPane.showMessageDialog(null, "O jogo ainda não foi concluído!"); 
            }
        });
        mainPanel.add(finishGameButton);
    }

    private void addCheckGameStatusButton(final JPanel mainPanel) {
        checkGameStatusButton = new CheckGameStatusButton(e -> {
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
    
            String message = switch (gameStatus) {
                case NON_STARTED -> "O jogo não foi iniciado!";
                case INCOMPLETE -> "O jogo está incompleto!";
                case COMPLETED -> "O jogo foi concluído!";
                default -> "qualquer coisa";
            } ;       

            message += hasErrors ? " Este jogo contém erros!" : " Este jogo não contém erros!";
            JOptionPane.showMessageDialog(null, message);
        });
        mainPanel.add(checkGameStatusButton);
    }

    private void addResetButton(final JPanel mainPanel) {
        resetButton =  new ResetButton(e ->  {
            var dialogResult = JOptionPane.showConfirmDialog(
                null, 
                "Deseja realmente reiniciar o jogo?", 
                "Reiniciar jogo", 
                YES_NO_OPTION, 
                QUESTION_MESSAGE
            );
            if (dialogResult == 0) {
                boardService.reset();
                notifierService.notify(CLEAR_SPACE);
            }           
        });
        mainPanel.add(resetButton);
    }

    private void addNewGameButton(final JPanel mainPanel) {
        newGameButton = new NewGameButton( e -> {
            var dialogResult = JOptionPane.showConfirmDialog(
                null, 
                "Deseja realmente um novo o jogo?", 
                "Novo jogo", 
                YES_NO_OPTION, 
                QUESTION_MESSAGE
            );
            if (dialogResult == 0) {
                newGame();
                notifierService.notify(CLEAR_SPACE);
            }    
        });
        mainPanel.add(newGameButton);
    }

    private void addLevelComboBox(final JPanel mainPanel) {
        String[] values = {"Fácil", "Médio", "Difícil"};
        levelComboBox = new LevelCombo(level, values, e -> {
            if (level == levelComboBox.getSelectedIndex()) return;

            var dialogResult = JOptionPane.showConfirmDialog(
                null, 
                "Deseja realmente mudar o nivel do jogo? Isso irá gerar um novo jogo", 
                "Mudar o nivel do jogo", 
                YES_NO_OPTION, 
                QUESTION_MESSAGE
            );
            if (dialogResult == 0) {                
                newGame();
                notifierService.notify(CLEAR_SPACE);
            }
            else{
                levelComboBox.setSelectedIndex(level);
            }
        });
        mainPanel.add(levelComboBox);
    }

    public void newGame() {
        if (nonNull(boardService)) {
            boardService.reset();  
        }
        this.level = levelComboBox.getSelectedIndex();
        Generator generator = new Generator(this.level);        
        var gameConfig = generator.getBoardAsMap();
        
        // Atualiza apenas os dados do BoardService
        boardService.updateBoard(gameConfig);
        mainPanel.removeAll();
        
        //addSpacesToMainPanel();
        buildMainScreen();
        
        // Notifica os componentes para atualizarem os valores
        notifierService.notify(CLEAR_SPACE);
    
        // Revalida e repinta a interface
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}
