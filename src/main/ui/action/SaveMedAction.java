package ui.action;

import persistance.JsonReader;
import persistance.JsonWriter;
import ui.MediManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

import static ui.MediManagerConsoleApp.JSON_STORE;

// Class represents the action to be taken when user hits save button
public class SaveMedAction extends AbstractAction {
    MediManagerGUI manager;
    JsonWriter writer;

    public SaveMedAction(MediManagerGUI manager, JsonWriter writer) {
        super("SAVE");
        this.manager = manager;
        this.writer = writer;
    }

    // EFFECTS: Saves user's medications to JSON_STORE and outputs a success message if successful, and failure
    // message if FileNotFoundException is caught
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            writer.open();
            writer.write(manager.getMedCab());
            writer.close();
            if (0 != JOptionPane.showConfirmDialog(null, "Are you sure you wish to overwrite"
                    + " your previous save?", "Overwrite save?", JOptionPane.YES_NO_CANCEL_OPTION)) {
                return;
            }
            System.out.println("Saved current MediManager configuration to: " + JSON_STORE);
            JOptionPane.showMessageDialog(null, "Save completed!", "SAVE COMPLETE",
                    JOptionPane.PLAIN_MESSAGE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
            JOptionPane.showMessageDialog(null, "Save failed.", "FAILURE",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
