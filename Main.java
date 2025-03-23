package main;

import gui.MainMenu;
import model.Inventory;

import javax.swing.*;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String csvFilePath = Paths.get("resources", "inventory.csv").toString();
        Inventory inventory = new Inventory(csvFilePath);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainMenu mainMenu = new MainMenu(inventory);
                mainMenu.setVisible(true);
            }
        });
    }
}