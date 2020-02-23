package wolcen.salarybillsender;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SendDataTableCellRenderer extends JComponent implements TableCellRenderer {
    private JLabel lbl;

    SendDataTableCellRenderer() {
        super();
        (this.lbl = new JLabel()).setEnabled(true);
        this.lbl.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        this.lbl.setText(String.valueOf(value));
        return this.lbl;
    }
}
