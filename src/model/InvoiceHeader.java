package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class InvoiceHeader {
    private long invoiceNum;
    private Date invoiceDate;
    private String customerName;
    private ArrayList<InvoiceLine> invoiceLines;

    public InvoiceHeader(long invoiceNum, Date invoiceDate, String customerName) {
        this.invoiceNum = invoiceNum;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
        this.invoiceLines = new ArrayList<>();
    }

    public long getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(long invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(ArrayList<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public Double getTotal() {
        return invoiceLines.stream().map(item -> item.getItemPrice() * item.getCount())
                .collect(Collectors.summarizingDouble(Double::doubleValue)).getSum();
    }

}
