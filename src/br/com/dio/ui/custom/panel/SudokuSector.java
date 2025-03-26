package br.com.dio.ui.custom.panel;

import javax.swing.JPanel;
import java.awt.Dimension;

public class SudokuSector extends JPanel{


    public SudokuSector() {
        var dimension = new Dimension(170, 170);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
    }
}
