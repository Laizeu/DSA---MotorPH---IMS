package util;

import model.Stock;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
    public static void writeStocksToCSV(String filePath, List<Stock> stocks) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Date Entered,Stock Label,Brand,Engine Number,Status\n");
            for (Stock stock : stocks) {
                writer.write(stock.getDateEntered() + "," +
                        stock.getStockLabel() + "," +
                        stock.getBrand() + "," +
                        stock.getEngineNumber() + "," +
                        stock.getStatus() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}