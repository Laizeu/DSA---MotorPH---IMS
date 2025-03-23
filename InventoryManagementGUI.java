package gui;

import model.Inventory;
import model.Stock;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InventoryManagementGUI extends JFrame {
    private Inventory inventory;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchCriteriaComboBox;
    private JSpinner datePicker;
    private SimpleDateFormat dateFormat;
    private TableRowSorter<DefaultTableModel> sorter;
    private boolean updatingRowNumbers = false; // Flag to avoid infinite loop

    public InventoryManagementGUI(Inventory inventory) {
        this.inventory = inventory;
        this.dateFormat = new SimpleDateFormat("MM/dd/yyyy"); // Define the date format

        setTitle("Inventory Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table model
        String[] columnNames = {"#", "Date", "Stock Label", "Brand", "Engine Number", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells not editable
            }
        };

        // Add TableModelListener to update the # column after any change
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (!updatingRowNumbers) {
                    updateRowNumbers();
                }
            }
        });

        inventoryTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        inventoryTable.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        add(scrollPane, BorderLayout.CENTER);

        // Set default sort order by Brand (column index 3)
        sorter.setSortKeys(List.of(new RowSorter.SortKey(3, SortOrder.ASCENDING)));

        // Top panel for search functionality
        JPanel topPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        // Include "Search by" as the first item in the search criteria combo box
        String[] searchCriteria = {"Search by", "Date", "Stock Label", "Brand", "Engine Number", "Status"};
        searchCriteriaComboBox = new JComboBox<>(searchCriteria);
        searchCriteriaComboBox.setSelectedIndex(0); // Set default to "Search by"

        // Date picker setup using JSpinner
        SpinnerDateModel dateModel = new SpinnerDateModel();
        datePicker = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(datePicker, "MM/dd/yyyy");
        datePicker.setEditor(dateEditor);
        datePicker.setVisible(false); // Initially hide the date picker

        searchCriteriaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchCriteriaComboBox.getSelectedItem().equals("Date")) {
                    searchField.setVisible(false);
                    datePicker.setVisible(true);
                } else {
                    searchField.setVisible(true);
                    datePicker.setVisible(false);
                }
                topPanel.revalidate();
                topPanel.repaint();
            }
        });

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStock();
            }
        });
        JButton showAllButton = new JButton("Show All");
        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAllStocks();
            }
        });

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchCriteriaComboBox, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(showAllButton, BorderLayout.EAST);
        topPanel.add(datePicker, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Bottom panel for add and remove buttons
        JPanel bottomPanel = new JPanel();
        JButton addButton = new JButton("Add Stock");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStock();
            }
        });
        JButton removeButton = new JButton("Remove Stock");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeStock();
            }
        });
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu(inventory).setVisible(true);
                dispose();
            }
        });

        bottomPanel.add(addButton);
        bottomPanel.add(removeButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        displayAllStocks();

        // Center the window
        setLocationRelativeTo(null);
    }

    private void addStock() {
        JTextField dateField = new JTextField();
        JComboBox<String> labelField = new JComboBox<>(new String[]{"Old", "New"});
        JComboBox<String> brandField = new JComboBox<>(new String[]{"Honda", "Kawasaki", "Kymco", "Suzuki", "Yamaha"});
        JTextField engineField = new JTextField();
        JComboBox<String> statusField = new JComboBox<>(new String[]{"On-hand", "Sold"});

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Date (mm/dd/yyyy):"));  // Indicating the correct format
        panel.add(dateField);
        panel.add(new JLabel("Stock Label:"));
        panel.add(labelField);
        panel.add(new JLabel("Brand:"));
        panel.add(brandField);
        panel.add(new JLabel("Engine Number:"));
        panel.add(engineField);
        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Stock", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validate date
                Date date = dateFormat.parse(dateField.getText());
                String formattedDate = dateFormat.format(date);

                // Validate stock label
                if (labelField.getSelectedItem() == null) {
                    throw new Exception("You need to select from the Stock Label dropdown.");
                }
                String label = (String) labelField.getSelectedItem();

                // Validate brand
                if (brandField.getSelectedItem() == null) {
                    throw new Exception("You need to select from the Brand dropdown.");
                }
                String brand = (String) brandField.getSelectedItem();

                // Validate engine number
                if (engineField.getText().trim().isEmpty()) {
                    throw new Exception("Engine Number field cannot be blank.");
                }
                String engine = engineField.getText().trim();

                // Validate status
                if (statusField.getSelectedItem() == null) {
                    throw new Exception("You need to select from the Status dropdown.");
                }
                String status = (String) statusField.getSelectedItem();

                Stock stock = new Stock(formattedDate, label, brand, engine, status);
                inventory.addStock(stock);
                displayAllStocks();

                // Show success message and highlight the new stock
                JOptionPane.showMessageDialog(this, "Stock added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                highlightNewStock(stock);

            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use mm/dd/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void highlightNewStock(Stock stock) {
        int rowCount = tableModel.getRowCount();
        int modelRow = -1;
        for (int i = 0; i < rowCount; i++) {
            if (tableModel.getValueAt(i, 4).equals(stock.getEngineNumber())) {
                modelRow = i;
                break;
            }
        }
        if (modelRow >= 0) {
            int viewRow = inventoryTable.convertRowIndexToView(modelRow);
            inventoryTable.setRowSelectionInterval(viewRow, viewRow);
            inventoryTable.scrollRectToVisible(new Rectangle(inventoryTable.getCellRect(viewRow, 0, true)));
        }
    }

    private void removeStock() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow != -1) {
            String engineNumber = (String) tableModel.getValueAt(selectedRow, 4);
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this stock?", "Remove Stock", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                inventory.removeStock(engineNumber);
                displayAllStocks();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a stock to remove.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchStock() {
        String criteria = (String) searchCriteriaComboBox.getSelectedItem();
        if (criteria.equals("Search by")) {
            JOptionPane.showMessageDialog(this, "Please select a search criterion.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String searchText = searchField.getText();
        List<Stock> stocks = inventory.getAllStocks();
        tableModel.setRowCount(0); // Clear the table

        String selectedDate = dateFormat.format((Date) datePicker.getValue());

        int listNumber = 1; // Initialize list number for search results

        for (Stock stock : stocks) {
            boolean matches = false;
            if (criteria.equals("Date") && stock.getDateEntered().contains(selectedDate)) {
                matches = true;
            } else if (criteria.equals("Stock Label") && stock.getStockLabel().contains(searchText)) {
                matches = true;
            } else if (criteria.equals("Brand") && stock.getBrand().contains(searchText)) {
                matches = true;
            } else if (criteria.equals("Engine Number") && stock.getEngineNumber().contains(searchText)) {
                matches = true;
            } else if (criteria.equals("Status") && stock.getStatus().contains(searchText)) {
                matches = true;
            }

            if (matches) {
                addSearchResultToTable(stock, listNumber++);
            }
        }
    }

    private void displayAllStocks() {
        tableModel.setRowCount(0); // Clear the table
        List<Stock> stocks = inventory.getAllStocks();
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            addStockToTable(stock, i + 1);
        }
    }

    private void addStockToTable(Stock stock, int number) {
        Object[] rowData = {
                number,
                stock.getDateEntered(),
                stock.getStockLabel(),
                stock.getBrand(),
                stock.getEngineNumber(),
                stock.getStatus()
        };
        tableModel.addRow(rowData);
    }

    private void addSearchResultToTable(Stock stock, int number) {
        Object[] rowData = {
                number,
                stock.getDateEntered(),
                stock.getStockLabel(),
                stock.getBrand(),
                stock.getEngineNumber(),
                stock.getStatus()
        };
        tableModel.addRow(rowData);
    }

    private void updateRowNumbers() {
        updatingRowNumbers = true;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(i + 1, i, 0);
        }
        updatingRowNumbers = false;
    }
}