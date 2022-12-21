package view;

import model.InvoiceHeader;
import model.InvoiceLine;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class InvoicesItemsTableModel extends AbstractTableModel {

    private static final String[] TABLE_COLUMNS = {"No.", "Item Name", "Item Price", "Count", "Item total"};

    private InvoiceHeader header;

    public InvoicesItemsTableModel(InvoiceHeader header) {
        this.header = header;
    }

    @Override
    public int getRowCount() {
        return header.getInvoiceLines().size();
    }

    @Override
    public int getColumnCount() {
        return TABLE_COLUMNS.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceLine item = header.getInvoiceLines().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return columnIndex;
            case 1:
                return item.getItemName();
            case 2:
                return item.getItemPrice();
            case 3:
                return item.getCount();
            case 4:
                return item.getTotal();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return TABLE_COLUMNS[column];
    }
}
