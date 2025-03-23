package gui;

import model.Inventory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    private Inventory inventory;

    public MainMenu(Inventory inventory) {
        this.inventory = inventory;
        setTitle("MotorPH Inventory Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JButton searchButton = new JButton("Search Stock");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InventoryManagementGUI(inventory).setVisible(true);
                dispose();
            }
        });

        JButton addButton = new JButton("Add Stock");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddStockGUI(inventory).setVisible(true);
                dispose();
            }
        });

        JButton removeButton = new JButton("Remove Stock");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RemoveStockGUI(inventory).setVisible(true);
                dispose();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(searchButton);
        add(addButton);
        add(removeButton);
        add(exitButton);

        // Center the window
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        Inventory inventory = new Inventory("resources/inventory.csv");
        MainMenu mainMenu = new MainMenu(inventory);
        mainMenu.setVisible(true);
    }
}