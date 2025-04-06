package br.com.dio.ui.custom.input;

import static java.util.Objects.isNull;

import java.util.List;

import javax.swing.text.PlainDocument;

public class NumberTextLimit extends PlainDocument{
    private final List<String> NUMBERS = List.of("1", "2","3", "4", "5", "6", "7", "8", "9");
    
    @Override
    public void insertString(int offset, String str, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
        if (isNull(str) || (!NUMBERS.contains(str))) return;
        
        if (getLength() + str.length() <= 1) {
            super.insertString(offset, str, attr);
        }
    }
}
