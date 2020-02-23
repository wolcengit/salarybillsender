package wolcen.salarybillsender;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

class DocumentFilter4UpperCharacter extends DocumentFilter {
    DocumentFilter4UpperCharacter() {
        super();
    }

    @Override
    public void insertString(final FilterBypass fp, final int offset, String string, final AttributeSet aset) throws BadLocationException {
        boolean isValidInput = true;
        if (string != null) {
            string = string.toUpperCase();
            for (int len = string.length(), i = 0; i < len; ++i) {
                if (string.charAt(i) < 'A' || string.charAt(i) > 'Z') {
                    isValidInput = false;
                    break;
                }
            }
        }
        if (isValidInput) {
            super.insertString(fp, offset, string, aset);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void replace(final FilterBypass fp, final int offset, final int length, String string, final AttributeSet aset) throws BadLocationException {
        boolean isValidInput = true;
        if (string != null) {
            string = string.toUpperCase();
            for (int len = string.length(), i = 0; i < len; ++i) {
                if (string.charAt(i) < 'A' || string.charAt(i) > 'Z') {
                    isValidInput = false;
                    break;
                }
            }
        }
        if (isValidInput) {
            super.replace(fp, offset, length, string, aset);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
