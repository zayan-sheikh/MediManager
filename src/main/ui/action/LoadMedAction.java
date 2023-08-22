package ui.action;

import model.Medication;
import persistance.JsonReader;
import persistance.JsonWriter;
import ui.MediManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

import static ui.MediManagerConsoleApp.JSON_STORE;

// Class represents the action to be taken when user hits load button
public class LoadMedAction extends AbstractAction {
    MediManagerGUI manager;
    JsonReader reader;

    // EFFECTS: constructs LoadMed action
    public LoadMedAction(MediManagerGUI manager, JsonReader reader) {
        super("LOAD");
        this.manager = manager;
        this.reader = reader;
    }

    // MODIFIES: medCab, displayList, medsModel
    // EFFECTS: Loads user's medications from JSON_STORE and outputs a success message if successful, and failure
    // message if FileNotFoundException is caught
    @Override
    public void actionPerformed(ActionEvent event) {
        int overwriting = 0;
        if (manager.getMedCab().getCabinet().size() > 0) {
            overwriting = JOptionPane.showConfirmDialog(null, "Are you sure you want to load"
                    + " your saved configuration? This will overwrite your current medications.");
        }
        if (overwriting == 0) {
            loadCabinet();
        }
        manager.informDetailWindow();
        manager.repaint();
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: loads values from file
    private void loadCabinet() {
        try {
            manager.setMedCab(reader.read());
            manager.getMedsModel().clear();
            for (Medication med : manager.getMedCab().getCabinet()) {
                manager.getMedsModel().addElement(med.getName());
            }
            manager.getDisplayedList().setModel(manager.getMedsModel());
            manager.revalidate();
            JOptionPane.showMessageDialog(null, "Loaded successfully!", "LOAD COMPLETE",
                    JOptionPane.PLAIN_MESSAGE);
            System.out.println("Loaded your MediManager configuration from: " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
            JOptionPane.showMessageDialog(null, "Load failed.", "FAILURE",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}