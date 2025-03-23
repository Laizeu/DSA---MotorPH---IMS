package gui;

import model.Inventory;
import model.Stock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddStockGUI extends JFrame {
    private Inventory inventory;
    private SimpleDateFormat dateFormat;

    public AddStockGUI(Inventory inventory) {
        this.inventory = inventory;
        this.dateFormat = new SimpleDateFormat("MM/dd/yyyy"); // Define the date format

        setTitle("Add Stock");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        JTextField dateField = new JTextField();
        JComboBox<String> labelField = new JComboBox<>(new String[]{"Old", "New"});
        JComboBox<String> brandField = new JComboBox<>(new String[]{"Honda", "Kawasaki", "Kymco", "Suzuki", "Yamaha"});
        JTextField engineField = new JTextField();
        JComboBox<String> statusField = new JComboBox<>(new String[]{"On-hand", "Sold"});

        add(new JLabel("Date (mm/dd/yyyy):"));
        add(dateField);
        add(new JLabel("Stock Label:"));
        add(labelField);
        add(new JLabel("Brand:"));
        add(brandField);
        add(new JLabel("Engine Number:"));
        add(engineField);
        add(new JLabel("Status:"));
        add(statusField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    JOptionPane.showMessageDialog(AddStockGUI.this, "Stock added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new InventoryManagementGUI(inventory).setVisible(true);
                    dispose();
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(AddStockGUI.this, "Invalid date format. Please use mm/dd/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AddStockGUI.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu(inventory).setVisible(true);
                dispose();
            }
        });

        add(addButton);
        add(cancelButton);

        // Center the window
        setLocationRelativeTo(null);
    }
}