package wolcen.salarybillsender;

import javax.swing.table.AbstractTableModel;

public class SendTableModel extends AbstractTableModel {
    private final String[] title;
    private SalarybillSender salarybillSender;

    public SendTableModel(final SalarybillSender salarybillSender) {
        super();
        this.title = new String[]{"     "};
        this.salarybillSender = salarybillSender;
    }

    @Override
    public int getRowCount() {
        return this.salarybillSender.getEmployees();
    }

    @Override
    public int getColumnCount() {
        return this.title.length;
    }

    @Override
    public String getColumnName(final int column) {
        return this.title[column];
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex == 0) {
            return this.salarybillSender.getTableCellContents(rowIndex);
        }
        return null;
    }
}
