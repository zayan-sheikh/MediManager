package ui.action;

import ui.MediManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

// Class determines course of action for remove button when pressed by user
public class RemoveMedAction extends AbstractAction {
    private MediManagerGUI manager;

    public RemoveMedAction(MediManagerGUI manager) {
        super("REMOVE MEDICATION");
        this.manager = manager;
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: dictates what happens when removeButton is pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        int index = manager.getDisplayedList().getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(null, "Please select a medication first!",
                    "CANNOT REMOVE MEDICATION", JOptionPane.ERROR_MESSAGE);
        } else {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove"
                    + " this medication?", "Remove medication?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == 0) {
                String name = manager.getMedCab().getCabinet().get(manager.getDisplayedList().getSelectedIndex())
                        .getName();
                manager.getMedCab().removeMedFromCab(manager.getMedCab().getCabinet().get(index));
                manager.getMedsModel().remove(manager.getDisplayedList().getSelectedIndex());
                manager.getDisplayedList().setModel(manager.getMedsModel());
                manager.revalidate();
                JOptionPane.showMessageDialog(null, name + " was removed successfully!",
                        "SUCCESSFULLY REMOVED MEDICATION", JOptionPane.PLAIN_MESSAGE);
            }
        }
        manager.informDetailWindow();
    }
}
