import controller.AppController;
import model.FileOperations;
import model.InvoiceHeader;
import model.InvoiceLine;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws ParseException, IOException {
        testDataPrint();
        AppController controller = new AppController();
        controller.startApp();
    }

    public static void testDataPrint() throws ParseException, IOException {
        FileOperations fileOperations = new FileOperations();
        ArrayList<InvoiceHeader> headers = fileOperations.readFile();
        StringBuilder builder = new StringBuilder();
        for (InvoiceHeader header : headers) {
            builder.append(header.getInvoiceNum())
                    .append("\n{\n")
                    .append(AppController.DATE_FORMAT.format(header.getInvoiceDate()))
                    .append(",")
                    .append(header.getCustomerName())
                    .append("\n");
            for (InvoiceLine line : header.getInvoiceLines()) {
                builder.append(line.getItemName())
                        .append(",")
                        .append(line.getItemPrice())
                        .append(",")
                        .append(line.getCount())
                        .append("\n");
            }
            builder.append("}\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        System.out.println(builder.toString());
    }
}