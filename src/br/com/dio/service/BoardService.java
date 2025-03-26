package br.com.dio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.dio.model.Board;
import br.com.dio.model.GameStatusEnum;
import br.com.dio.model.Space;

public class BoardService {
    private final static int BORD_LIMIT = 9;
    private final Board board;

    public BoardService(final Map<String, String> gameConfig)  {        
        board = new Board(initBoard(gameConfig));
    }

    public List<List<Space>> getSpaces() {        
        return board.getSpaces();
    }

    public void reset(){
        this.board.reset();
    }

    public boolean hasErrors(){
        return board.hasError();
    }

    public GameStatusEnum getStatus() {
        return board.getStatus();
    }

    public boolean isCompleted() {
        return board.isCompleted();
    }

    public void changeValue(final int col, final int row, final int value){
        board.changeValue(col, row, value);
    }

    private List<List<Space>> initBoard(Map<String, String> gameConfig) {
        List<List<Space>> spaces = new ArrayList<>();
        
        for (int i = 0; i < BORD_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BORD_LIMIT; j++) {
                var positionConfig = gameConfig.get("%s,%s".formatted(i, j));
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);

                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }            
        }
        return spaces;
    }

}
