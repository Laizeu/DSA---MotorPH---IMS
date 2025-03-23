package util;

import model.Stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static List<Stock> readStocksFromCSV(String filePath) {
        List<Stock> stocks = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 5) {
                    Stock stock = new Stock(values[0], values[1], values[2], values[3], values[4]);
                    stocks.add(stock);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stocks;
    }
}