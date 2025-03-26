import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

public class App {
    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int BORD_LIMIT = 9;

    public static void main(String[] args) throws Exception {
        var gameFixed = "0,0;4,false 1,0;7,false 2,0;9,true 3,0;5,false 4,0;8,true 5,0;6,true 6,0;2,true 7,0;3,false 8,0;1,false 0,1;1,false 1,1;3,true 2,1;5,false 3,1;4,false 4,1;7,true 5,1;2,false 6,1;8,false 7,1;9,true 8,1;6,true 0,2;2,false 1,2;6,true 2,2;8,false 3,2;9,false 4,2;1,true 5,2;3,false 6,2;7,false 7,2;4,false 8,2;5,true 0,3;5,true 1,3;1,false 2,3;3,true 3,3;7,false 4,3;6,false 5,3;4,false 6,3;9,false 7,3;8,true 8,3;2,false 0,4;8,false 1,4;9,true 2,4;7,false 3,4;1,true 4,4;2,true 5,4;5,true 6,4;3,false 7,4;6,true 8,4;4,false 0,5;6,false 1,5;4,true 2,5;2,false 3,5;3,false 4,5;9,false 5,5;8,false 6,5;1,true 7,5;5,false 8,5;7,true 0,6;7,true 1,6;5,false 2,6;4,false 3,6;2,false 4,6;3,true 5,6;9,false 6,6;6,false 7,6;1,true 8,6;8,false 0,7;9,true 1,7;8,true 2,7;1,false 3,7;6,false 4,7;4,true 5,7;7,false 6,7;5,false 7,7;2,true 8,7;3,false 0,8;3,false 1,8;2,false 2,8;6,true 3,8;8,true 4,8;5,true 5,8;1,false 6,8;4,true 7,8;7,false 8,8;9,false";
        String[] list = gameFixed.split(" ");

        final var positions = Stream.of(list).
            collect(Collectors.toMap(k -> k.split(";")[0], 
                                     v -> v.split(";")[1]));

        var option =-1;
        while (option != 8) {
            System.out.println("Selecione uma opção:");
            System.out.println("1 - Iniciar ");
            System.out.println("2 - Colocar um número ");
            System.out.println("3 - Limpar um número ");
            System.out.println("4 - Visualizar tabuleiro ");
            System.out.println("5 - Verificar se o jogo acabou");
            System.out.println("6 - Limpar jogo");
            System.out.println("7 - Finalizar");
            System.out.println("8 - Sair");


            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> clearNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);               
                default -> System.out.println("Opção inválida");                            
            }
        }
    }
      
    private static void startGame(final Map<String, String> positions) {
        if (nonNull(board)){
            System.out.println("Jogo já foi iniciado!");
            return;
        }
        List<List<Space>> spaces = new ArrayList<>();
        
        for (int i = 0; i < BORD_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BORD_LIMIT; j++) {
                var positionConfig = positions.get("%s,%s".formatted(i, j));
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);

                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }            
        }
        board = new Board(spaces);
        System.out.println("Jogo iniciado!");        
    }      
    
    private static void inputNumber() {
        
        if (isNull(board)){            
            System.out.println("Jogo ainda nao foi iniciado!");
            return;
        }
        System.out.println("Informe a coluna:");
        var col = runUntilGetValidNumber(0, BORD_LIMIT - 1);
        System.out.println("Informe a linha:");
        var row = runUntilGetValidNumber(0, BORD_LIMIT - 1);
        System.out.println("Informe o número:");
        var value = runUntilGetValidNumber(1, BORD_LIMIT);
        
        if (!board.changeValue(col, row, value)) {
            System.out.println("Posição inválida!");
        }
    }

    private static void clearNumber() {
        if (isNull(board)){            
            System.out.println("Jogo ainda nao foi iniciado!");
            return;
        }
        System.out.println("Informe a coluna:");
        var col = runUntilGetValidNumber(0, BORD_LIMIT - 1);
        System.out.println("Informe a linha:");
        var row = runUntilGetValidNumber(0, BORD_LIMIT - 1);
        
        if (!board.clearValue(col, row)) {
            System.out.println("Posição inválida!");
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)){            
            System.out.println("Jogo ainda nao foi iniciado!");
            return;
        }
        var args = new Object[81];
        var argPos = 0;

        //coluna
        for (int i = 0; i < BORD_LIMIT; i++) {
            //linha
            for (var col:board.getSpaces()) {                
                args[argPos ++] = " " + (isNull(col.get(i).getActual()) ? " " : col.get(i).getActual());
            }
        }
        System.out.printf((BOARD_TEMPLATE) + "%n", args);
    }

    private static void showGameStatus() {
        if (isNull(board)){            
            System.out.println("Jogo ainda nao foi iniciado!");
            return;
        }
        System.out.printf("O jogo está no status: %s\n", board.getStatus().getLabel());
        if (board.hasError()){
            System.out.println("Jogo com erro!");
        }else{
            System.out.println("Jogo sem erro!");
        }
    }

    private static void clearGame() {
        if (isNull(board)){            
            System.out.println("Jogo ainda nao foi iniciado!");
            return;
        }
        System.out.println("Tem certeza que deseja limpar seu jogo? Perderá todo seu progresso.");
        var confirm = scanner.next().toLowerCase();
        while ((!confirm.equalsIgnoreCase("sim")) && (!confirm.equalsIgnoreCase("não")) ){
            System.out.println("Informe sim ou não:");
            confirm = scanner.next().toLowerCase();            
        }
        if (confirm.equalsIgnoreCase("sim")){
            board.reset();
        }
    }

    private static void finishGame() {
        if (isNull(board)){            
            System.out.println("Jogo ainda nao foi iniciado!");
            return;
        }

        if (board.isCompleted()){
            System.out.println("Parabéns, você ganhou!");
            showCurrentGame();
            board = null;
        }else 
        if (board.hasError()){
            System.out.println("Seu jogo contém erros! Verifique o tabuleiro.");            
        }
        else{
            System.out.println("Ainda possui espaço(s) em branco");
        }
    }
    
    private static int runUntilGetValidNumber(final int min, final int max) {
        //validar que o user só pode inserir números

        var current = scanner.nextInt();
        while (current < min || current > max) {
            System.out.println("Informe um número entre %s e %s: ".formatted(min, max));
            current = scanner.nextInt();
        }
        return current;
    }
}

//