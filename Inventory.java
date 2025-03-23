package model;

import util.CSVReader;
import util.CSVWriter;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Inventory {
    private AVLTree<Stock> stockAVLTree;
    private HashMap<String, Stock> stockHashMap;
    private ArrayList<Stock> stockArrayList;
    private LinkedList<Stock> stockLinkedList;
    private String csvFilePath;
    private Lock lock;

    public Inventory(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        this.stockAVLTree = new AVLTree<>();
        this.stockHashMap = new HashMap<>();
        this.stockArrayList = new ArrayList<>();
        this.stockLinkedList = new LinkedList<>();
        this.lock = new ReentrantLock();
        loadFromCSV();
    }

    public void addStock(Stock stock) {
        lock.lock();
        try {
            if (stockHashMap.containsKey(stock.getEngineNumber())) {
                // Update existing stock
                Stock existingStock = stockHashMap.get(stock.getEngineNumber());
                existingStock.setDateEntered(stock.getDateEntered());
                existingStock.setStockLabel(stock.getStockLabel());
                existingStock.setBrand(stock.getBrand());
                existingStock.setStatus(stock.getStatus());
                System.out.println("Updated existing stock: " + stock.getEngineNumber());
            } else {
                // Add new stock
                stockAVLTree.insert(stock);
                stockHashMap.put(stock.getEngineNumber(), stock);
                stockArrayList.add(stock);
                stockLinkedList.add(stock);
                System.out.println("Added new stock: " + stock.getEngineNumber());
            }
            saveToCSV();
        } finally {
            lock.unlock();
        }
    }

    public void removeStock(String engineNumber) {
        lock.lock();
        try {
            Stock stock = stockHashMap.get(engineNumber);
            if (stock != null) {
                stockAVLTree.delete(stock);
                stockHashMap.remove(engineNumber);
                stockArrayList.remove(stock);
                stockLinkedList.remove(stock);
                System.out.println("Removed stock: " + engineNumber);
                saveToCSV();
            } else {
                System.out.println("Attempted to remove non-existing stock: " + engineNumber);
            }
        } finally {
            lock.unlock();
        }
    }

    public Stock searchStock(String engineNumber) {
        return stockHashMap.get(engineNumber);
    }

    public List<Stock> getAllStocks() {
        return new ArrayList<>(stockHashMap.values());
    }

    private void loadFromCSV() {
        System.out.println("Loading from CSV: " + csvFilePath);
        List<Stock> stocks = CSVReader.readStocksFromCSV(csvFilePath);
        for (Stock stock : stocks) {
            System.out.println("Loaded stock: " + stock.toString());
            addStock(stock);
        }
    }

    private void saveToCSV() {
        List<Stock> allStocks = getAllStocks();
        System.out.println("Saving to CSV: " + csvFilePath);
        for (Stock stock : allStocks) {
            System.out.println("Saving stock: " + stock.toString());
        }
        CSVWriter.writeStocksToCSV(csvFilePath, allStocks);
    }
}