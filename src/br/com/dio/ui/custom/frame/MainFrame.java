package br.com.dio.ui.custom.frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;

public class MainFrame extends JFrame{
    public MainFrame(final Dimension dimension, final JPanel mainPanel){
        super("Sudoku");        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.add(mainPanel);
    }
}
