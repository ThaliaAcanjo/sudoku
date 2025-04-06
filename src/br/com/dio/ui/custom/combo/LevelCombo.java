package br.com.dio.ui.custom.combo;

import java.awt.Dimension;

import javax.swing.JComboBox;
import java.awt.event.ActionListener;

public class LevelCombo extends JComboBox<String> {

    public LevelCombo(Integer index, String[]  values, final ActionListener actionListener) {                 
        super(values);
        Dimension dimension = new Dimension(60, 30);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setVisible(true);
        this.setSelectedIndex(index);
        this.addActionListener(actionListener);
    }
}
