package br.com.dio.ui.custom.button;

import javax.swing.JButton;
import java.awt.event.ActionListener;

public class NewGameButton  extends JButton {

    public NewGameButton(final ActionListener actionListener) {
        this.setText("Novo jogo");        
        this.addActionListener(actionListener);
    }

}
