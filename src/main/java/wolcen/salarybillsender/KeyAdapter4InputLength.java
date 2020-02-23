package wolcen.salarybillsender;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyAdapter4InputLength extends KeyAdapter {
    private int inputLength;
    private JTextField tfInput;

    public KeyAdapter4InputLength(final int inputLength, final JTextField tfInput) {
        super();
        this.inputLength = inputLength;
        this.tfInput = tfInput;
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        final int code = e.getKeyChar();
        if (code == 8) {
            e.isActionKey();
        } else if (this.tfInput.getDocument().getLength() >= this.inputLength) {
            if (null != this.tfInput.getSelectedText()) {
                e.isActionKey();
            } else {
                e.consume();
            }
        }
    }
}
