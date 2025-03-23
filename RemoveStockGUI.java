package gui;

import model.Inventory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveStockGUI extends JFrame {
    private Inventory inventory;

    public RemoveStockGUI(Inventory inventory) {
        this.inventory = inventory;

        setTitle("Remove Stock");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        JTextField engineField = new JTextField();

        add(new JLabel("Engine Number:"));
        add(engineField);

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String engineNumber = engineField.getText().trim();
                if (engineNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(RemoveStockGUI.this, "Engine Number field cannot be blank.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                inventory.removeStock(engineNumber);
                JOptionPane.showMessageDialog(RemoveStockGUI.this, "Stock removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new InventoryManagementGUI(inventory).setVisible(true);
                dispose();
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

        add(removeButton);
        add(cancelButton);

        // Center the window
        setLocationRelativeTo(null);
    }
}