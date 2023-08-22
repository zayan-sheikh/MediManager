package ui.action;

import ui.MediManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

// class for the action of the clear med button
public class ClearMedAction extends AbstractAction {
    private MediManagerGUI manager;

    // EFFECTS: clearMed action constructor
    public ClearMedAction(MediManagerGUI manager) {
        super("CLEAR ALL MEDICATIONS");
        this.manager = manager;
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: dictates what happens when clearButton is pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        if (0 == JOptionPane.showConfirmDialog(null, "Are you sure you wish to clear ALL of"
                + " your saved medications?")) {
            manager.getMedCab().clearCabinet();
            manager.getMedsModel().clear();
            manager.informDetailWindow();
            manager.repaint();
        }
    }
}
