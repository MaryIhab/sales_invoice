package model;

import controller.AppController;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FileOperations {

    private static final String HEADERS_FILE_PATH = "./InvoiceHeader.csv";
    private static final String LINES_FILE_PATH = "./InvoiceLine.csv";

    public ArrayList<InvoiceHeader> readFile() throws ParseException, IOException {
        ArrayList<InvoiceHeader> headers = new ArrayList<>();
        ArrayList<InvoiceLine> lines = new ArrayList<>();


        ArrayList<String[]> headerFileLines = readFileLines(HEADERS_FILE_PATH);
        ArrayList<String[]> itemsFileLines = readFileLines(LINES_FILE_PATH);


        for (String[] line : headerFileLines) {
            InvoiceHeader header = null;
                header = new InvoiceHeader(
                        Long.parseLong(line[0]),
                        AppController.DATE_FORMAT.parse(line[1]),
                        line[2]
                );
                for (String[] itemLine : itemsFileLines) {
                    //Get only items of the current invoice
                    if (itemLine[0].equals(line[0])) {
                        header.getInvoiceLines().add(
                                new InvoiceLine(
                                        itemLine[1],
                                        Double.parseDouble(itemLine[2]),
                                        Integer.parseInt(itemLine[3])
                                )
                        );
                    }
                }
            headers.add(header);
        }
        return headers;
    }

    public void writeFile(ArrayList<InvoiceHeader> headers) throws IOException {
            FileWriter headersWriter = new FileWriter(HEADERS_FILE_PATH);
            FileWriter linesWriter = new FileWriter(LINES_FILE_PATH);
            StringBuilder headersString = new StringBuilder();
            StringBuilder linesString = new StringBuilder();
            for (InvoiceHeader invoiceHeader : headers) {
                headersString.append(invoiceHeader.getInvoiceNum()).append(",")
                        .append(AppController.DATE_FORMAT.format(invoiceHeader.getInvoiceDate())).append(",")
                        .append(invoiceHeader.getCustomerName()).append("\n");
                for(InvoiceLine line: invoiceHeader.getInvoiceLines()){
                    linesString.append(invoiceHeader.getInvoiceNum()).append(",")
                            .append(line.getItemName())
                            .append(",")
                            .append(line.getItemPrice())
                            .append(",")
                            .append(line.getCount())
                            .append("\n");
                }
            }
            headersWriter.write(headersString.toString());
            linesWriter.write(linesString.toString());
            headersWriter.close();
            linesWriter.close();
    }

    private ArrayList<String[]> readFileLines(String fileName) throws IOException {
        ArrayList<String[]> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line.split(","));
            }
        } catch (IOException e) {
            throw e;
        }
        return lines;
    }

}
