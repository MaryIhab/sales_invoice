package view;

import controller.AppController;
import model.InvoiceHeader;
import model.InvoiceLine;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class AppGUI {

    private AppController actionListener;
    private JTable headersTable;
    private JPanel rightPanel;

    private int selectedInvoice = -1;
    private JFrame jFrame;
    private int selectedIem;
    private JTextField invoiceNameField;
    private JTextField invoiceDateField;

    public AppGUI(AppController actionListener) {
        this.actionListener = actionListener;
    }

    public void showApp() {
        jFrame = new JFrame();
        jFrame.setJMenuBar(getMenuBar());
        jFrame.add(getPanels());
        jFrame.setSize(1200, 700);
        jFrame.setVisible(true);
    }

    private JMenuBar getMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadFile = fileMenu.add(new JMenuItem("Load File"));
        loadFile.addActionListener(actionListener);
        JMenuItem saveFile = fileMenu.add(new JMenuItem("Save File"));
        saveFile.addActionListener(actionListener);
        jMenuBar.add(fileMenu);
        return jMenuBar;
    }

    private JSplitPane getPanels() {
        JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainPanel.setDividerLocation(600);
        mainPanel.add(getLeftPanel());
        mainPanel.add(getRightPanel());
        return mainPanel;
    }

    private JPanel getLeftPanel() {
        TitledBorder titledBorder = new TitledBorder("Invoices : ");
        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(titledBorder);
        leftPanel.add(getInvoicesTablePane());
        JButton deleteButton = new JButton("Delete Invoice");
        deleteButton.addActionListener(actionListener);
        JButton createButton = new JButton("Create New Invoice");
        createButton.addActionListener(actionListener);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(createButton);
        buttonsPanel.add(deleteButton);
        leftPanel.add(buttonsPanel);
        return leftPanel;
    }

    private JPanel getRightPanel() {
        rightPanel = new JPanel();
        rightPanel.add(new JLabel("Invoice Info"));
        return rightPanel;
    }

    private JScrollPane getInvoicesTablePane() {
        headersTable = new JTable();
        headersTable.setBounds(30, 40, 200, 300);
        headersTable.getSelectionModel().addListSelectionListener(e -> {
            selectedInvoice = headersTable.getSelectedRow();
            System.out.println("Selected invoice changed : " + selectedInvoice);
            actionListener.invoiceSelected(selectedInvoice);
        });
        JScrollPane pane = new JScrollPane(headersTable);
        return pane;
    }

    public void loadInvoices(ArrayList<InvoiceHeader> invoiceHeaders) {
        headersTable.setModel(new InvoicesTableModel(invoiceHeaders));
    }

    public void changeInvoicesSelection(int index) {
        headersTable.changeSelection(index, 0, false, false);
    }

    public void loadInvoiceInfo(InvoiceHeader invoiceHeader) {
        rightPanel.removeAll();
        rightPanel.add(getInvoiceDataPanel(invoiceHeader));
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private JPanel getInvoiceDataPanel(InvoiceHeader invoiceHeader) {
        JPanel infoTablePanel = new JPanel();
        JPanel infoPanel = new JPanel();
        infoTablePanel.setLayout(new BoxLayout(infoTablePanel, BoxLayout.Y_AXIS));

        GridLayout layout = new GridLayout(6, 2);
        infoPanel.setLayout(layout);

        infoPanel.add(new JLabel("Invoice number:", SwingConstants.LEFT));
        infoPanel.add(new JLabel(String.valueOf(invoiceHeader.getInvoiceNum())));
        infoPanel.add(new JLabel("Invoice Date: ", SwingConstants.LEFT));
        invoiceDateField = new JTextField(AppController.DATE_FORMAT.format(invoiceHeader.getInvoiceDate()));
        infoPanel.add(invoiceDateField);
        infoPanel.add(new JLabel("Customer Name: ", SwingConstants.LEFT));
        invoiceNameField = new JTextField(invoiceHeader.getCustomerName().toString());
        infoPanel.add(invoiceNameField);
        infoPanel.add(new JLabel("Invoice Total: ", SwingConstants.LEFT));
        infoPanel.add(new JLabel(String.valueOf(invoiceHeader.getTotal())));

        TitledBorder titledBorder = new TitledBorder("Invoices Items");
        JTable itemsTable = new JTable();
        itemsTable.setBounds(30, 40, 200, 300);
        itemsTable.setModel(new InvoicesItemsTableModel(invoiceHeader));

        itemsTable.getSelectionModel().addListSelectionListener(e -> selectedIem = itemsTable.getSelectedRow());

        JScrollPane pane = new JScrollPane(itemsTable);
        pane.setBorder(titledBorder);

        infoTablePanel.add(infoPanel);
        infoTablePanel.add(pane);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(actionListener);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(actionListener);
        JButton createItemButton = new JButton("Create Item");
        createItemButton.addActionListener(actionListener);
        JButton deleteItemButton = new JButton("Delete Item");
        deleteItemButton.addActionListener(actionListener);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(createItemButton);
        buttonsPanel.add(deleteItemButton);

        infoTablePanel.add(buttonsPanel);


        return infoTablePanel;
    }

    public int getSelectedInvoice() {
        return selectedInvoice;
    }

    public boolean showConfirmationDialog(String message) {
        int a = JOptionPane.showConfirmDialog(jFrame, message, "Confirmation", JOptionPane.YES_NO_OPTION);
        return a == JOptionPane.YES_OPTION;
    }

    public InvoiceHeader showInvoiceInputDialog() throws ParseException {
        JTextField nameField = new JTextField(15);
        JFormattedTextField dateField = new JFormattedTextField(AppController.DATE_FORMAT);
        dateField.setColumns(15);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Name :"));
        inputPanel.add(nameField);
        inputPanel.add(Box.createHorizontalStrut(15)); // a spacer
        inputPanel.add(new JLabel("Date :"));
        inputPanel.add(dateField);
        int result = JOptionPane.showConfirmDialog(null, inputPanel,
                "Please Enter Invoice Info", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            InvoiceHeader header = new InvoiceHeader(
                    -1, AppController.DATE_FORMAT.parse(dateField.getText()), nameField.getText()
            );
            return header;
        }
        return null;
    }

    public InvoiceLine showItemInputDialog() {
        JTextField nameField = new JTextField(15);
        SpinnerModel countModel = new SpinnerNumberModel(0, //initial value
                0, //minimum value
                Integer.MAX_VALUE, //maximum value
                1); //step
        JSpinner countSpinner = new JSpinner(countModel);
        SpinnerModel priceModel = new SpinnerNumberModel(0, //initial value
                0, //minimum value
                Integer.MAX_VALUE, //maximum value
                0.1); //step
        JSpinner priceSpinner = new JSpinner(priceModel);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Name :"));
        inputPanel.add(nameField);
        inputPanel.add(Box.createHorizontalStrut(15)); // a spacer
        inputPanel.add(new JLabel("Price :"));
        inputPanel.add(priceSpinner);
        inputPanel.add(new JLabel("Count :"));
        inputPanel.add(countSpinner);

        int result = JOptionPane.showConfirmDialog(null, inputPanel,
                "Please Enter Item Info", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            InvoiceLine line = new InvoiceLine(
                    nameField.getText(),
                    Double.parseDouble(priceSpinner.getValue().toString()),
                    Integer.parseInt(countSpinner.getValue().toString())
            );
            return line;
        }
        return null;
    }

    public void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(jFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public int getSelectedItem() {
        return this.selectedIem;
    }

    public String getUpdatedInvoiceName(){
        return invoiceNameField.getText();
    }

    public String getUpdatedInvoiceDate(){
        return invoiceDateField.getText();
    }
}
