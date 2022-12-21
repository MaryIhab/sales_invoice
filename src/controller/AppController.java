package controller;

import model.FileOperations;
import model.InvoiceHeader;
import model.InvoiceLine;
import view.AppGUI;

import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AppController implements ActionListener {

    private FileOperations fileOperations;
    private AppGUI gui;
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    private ArrayList<InvoiceHeader> invoiceHeaders;

    public AppController() {
        this.fileOperations = new FileOperations();
        this.invoiceHeaders = new ArrayList<>();
    }

    public void startApp() {
        gui = new AppGUI(this);
        gui.showApp();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Load File":
                System.out.println("Loading file...");
                try {
                    this.invoiceHeaders = fileOperations.readFile();
                } catch (NumberFormatException ex) {
                    this.gui.showErrorDialog("Error reading the file - Invalid number format \n" + ex.getMessage());
                } catch (ParseException ex) {
                    this.gui.showErrorDialog("Error reading the file - Invalid date format \n" + ex.getMessage());
                } catch (IOException ex) {
                    this.gui.showErrorDialog("Error reading the file - I/O error \n" + ex.getMessage());
                }
                gui.loadInvoices(this.invoiceHeaders);
                break;
            case "Save File":
                System.out.println("Saving file...");
                try {
                    fileOperations.writeFile(this.invoiceHeaders);
                } catch (IOException ex) {
                    this.gui.showErrorDialog("Error writing to file! \n " + ex.getMessage());
                }
                break;
            case "Delete Invoice":
                System.out.println("Deleting Invoice");
                if (this.gui.showConfirmationDialog("Are you sure you want to delete invoice?")) {
                    this.deleteInvoice(gui.getSelectedInvoice());
                }
                break;
            case "Create New Invoice":
                InvoiceHeader invoiceHeader = null;
                try {
                    invoiceHeader = this.gui.showInvoiceInputDialog();
                } catch (ParseException ex) {
                    this.gui.showErrorDialog("Wrong Date format");
                }
                if (invoiceHeader != null) {
                    createInvoice(invoiceHeader);
                }
                break;
            case "Create Item":
                InvoiceLine line;
                line = this.gui.showItemInputDialog();
                if (line != null) {
                    createItem(line);
                }
                break;
            case "Delete Item":
                if (this.gui.showConfirmationDialog("Are you sure you want to delete this item?")) {
                    this.deleteItem(gui.getSelectedItem(), gui.getSelectedInvoice());
                }
                break;
            case "Save":
                System.out.println("Updating invoice info...");
                try {
                    updateInvoiceInfo(this.gui.getUpdatedInvoiceName(), this.gui.getUpdatedInvoiceDate());
                } catch (ParseException ex) {
                    this.gui.showErrorDialog("Wrong Date format");
                }
                break;
            case "Cancel":
                System.out.println("Resetting invoice info...");
                this.gui.loadInvoiceInfo(invoiceHeaders.get(gui.getSelectedInvoice()));
                break;
            default:
                System.out.println("Unhandled Command!");
        }
    }

    private void updateInvoiceInfo(String name, String date) throws ParseException {
        int index = gui.getSelectedInvoice();
        this.invoiceHeaders.get(index).setInvoiceDate(DATE_FORMAT.parse(date));
        this.invoiceHeaders.get(index).setCustomerName(name);
        this.gui.loadInvoices(invoiceHeaders);
        this.gui.changeInvoicesSelection(index);
        this.gui.loadInvoiceInfo(invoiceHeaders.get(index));
    }

    private void createItem(InvoiceLine line) {
        int index = gui.getSelectedInvoice();
        this.invoiceHeaders.get(index).getInvoiceLines().add(line);
        this.gui.loadInvoices(invoiceHeaders);
        this.gui.changeInvoicesSelection(index);
        this.gui.loadInvoiceInfo(invoiceHeaders.get(this.gui.getSelectedInvoice()));
    }

    private void createInvoice(InvoiceHeader invoiceHeader) {
        Long maxNumber = invoiceHeaders.stream().mapToLong(InvoiceHeader::getInvoiceNum).max().orElse(0) + 1;
        invoiceHeader.setInvoiceNum(maxNumber);
        invoiceHeaders.add(invoiceHeader);
        this.gui.loadInvoices(invoiceHeaders);
    }

    public void invoiceSelected(int index) {
        if (index >= 0 && index < invoiceHeaders.size()) {
            this.gui.loadInvoiceInfo(invoiceHeaders.get(index));
        }
    }

    public void deleteInvoice(int index) {
        if (index >= 0 && index < invoiceHeaders.size()) {
            this.invoiceHeaders.remove(index);
            this.gui.loadInvoices(invoiceHeaders);
        }
    }

    public void deleteItem(int index, int invoiceIndex) {
        if (index >= 0 && index < this.invoiceHeaders.get(invoiceIndex).getInvoiceLines().size()) {
            this.invoiceHeaders.get(invoiceIndex).getInvoiceLines().remove(index);
            this.gui.loadInvoices(invoiceHeaders);
            this.gui.changeInvoicesSelection(invoiceIndex);
            this.gui.loadInvoiceInfo(invoiceHeaders.get(invoiceIndex));
        }
    }

}
