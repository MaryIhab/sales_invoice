package model;

public class InvoiceLine {
    private String itemName;
    private double itemPrice;
    private int count;

//    private Long invoiceNumber;

    public InvoiceLine(String itemName, double itemPrice, int count) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Double getTotal() {
        return count * itemPrice;
    }

//    public Long getInvoiceNumber() {
//        return invoiceNumber;
//    }
//
//    public void setInvoiceNumber(Long invoiceNumber) {
//        this.invoiceNumber = invoiceNumber;
//    }
}
