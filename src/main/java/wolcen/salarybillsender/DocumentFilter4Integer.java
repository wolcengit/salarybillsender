package wolcen.salarybillsender;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class DocumentFilter4Integer extends DocumentFilter {
    public DocumentFilter4Integer() {
        super();
    }

    @Override
    public void insertString(final FilterBypass fp, final int offset, final String string, final AttributeSet aset) throws BadLocationException {
        final int len = string.length();
        boolean isValidInput = true;
        for (int i = 0; i < len; ++i) {
            if (!Character.isDigit(string.charAt(i))) {
                isValidInput = false;
                break;
            }
        }
        if (isValidInput) {
            super.insertString(fp, offset, string, aset);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void replace(final FilterBypass fp, final int offset, final int length, final String string, final AttributeSet aset) throws BadLocationException {
        boolean isValidInput = true;
        if (string != null) {
            for (int len = string.length(), i = 0; i < len; ++i) {
                if (!Character.isDigit(string.charAt(i))) {
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
