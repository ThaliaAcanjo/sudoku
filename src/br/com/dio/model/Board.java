package br.com.dio.model;


import static br.com.dio.model.GameStatusEnum.COMPLETED;
import static br.com.dio.model.GameStatusEnum.INCOMPLETE;
import static br.com.dio.model.GameStatusEnum.NON_STARTED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.Collection;
import java.util.List;

public class Board {
    private final List<List<Space>> spaces;
    /*List<List<Space>> spaces
     * List<... -> coluna
     * <List<Space >> -> linha
     */

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces; 
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatusEnum getStatus() {
        if (spaces.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixed()  && nonNull(s.getActual()))) {
            return NON_STARTED;
        }
        return spaces.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getActual())) ? INCOMPLETE : COMPLETED;
    }

    public boolean hasError() {
        if (getStatus() == NON_STARTED)
            return false;
                
        // for no spaces para validar qual está errado
        // for (var space : spaces) {
        //     for (var s : space) {
        //         if (!s.isFixed() && 
        //             nonNull(s.getActual()) && 
        //             !s.getActual().equals(s.getExpected())) {
        //             return true;
        //         }
        //     }
        
        // }

        return spaces.stream().
                    flatMap(Collection::stream).
                    anyMatch(s -> nonNull(s.getActual()) && 
                             !s.getActual().equals(s.getExpected()));
    }

    public boolean changeValue(final int col, final int row, final int value){
        var space = spaces.get(col).get(row);

        if (space.isFixed()) return false;

        space.setActual(value);
        return true;
    }

    public boolean clearValue(final int col, final int row){
        var space = spaces.get(col).get(row);
        if (space.isFixed()) return false;
        space.cleanSpace();
        return true;
    }
    
    public void reset() {
        spaces.forEach(c -> c.forEach(Space::cleanSpace));
        //spaces.stream().flatMap(Collection::stream).forEach(Space::cleanSpace);
    }

    public boolean isCompleted() {
        return  !hasError() && getStatus() == COMPLETED;
    }
}
