package view;

import controller.AppController;
import model.InvoiceHeader;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class InvoicesTableModel extends AbstractTableModel{

    private static final String[] TABLE_COLUMNS = {"No.","Date","Customer","Total"};

    private ArrayList<InvoiceHeader> invoices;

    public InvoicesTableModel(ArrayList<InvoiceHeader> invoices) {
        this.invoices = invoices;
    }

    @Override
    public int getRowCount() {
        return invoices.size();
    }

    @Override
    public int getColumnCount() {
        return InvoiceHeader.class.getDeclaredFields().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader invoice = invoices.get(rowIndex);
        switch (columnIndex){
            case 0:
                return invoice.getInvoiceNum();
            case 1:
                return AppController.DATE_FORMAT.format(invoice.getInvoiceDate());
            case 2:
                return invoice.getCustomerName();
            case 3:
                return invoice.getInvoiceLines().size();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return TABLE_COLUMNS[column];
    }
}
