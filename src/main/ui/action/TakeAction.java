package ui.action;

import ui.MediManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TakeAction extends AbstractAction {
    private MediManagerGUI manager;

    // EFFECTS: clearMed action constructor
    public TakeAction(MediManagerGUI manager) {
        super("Take Medication");
        this.manager = manager;
    }

    // REQUIRES: index > -1
    // MODIFIES: this, MediManagerGUI
    // EFFECTS: takes pill upon button press through this action
    @Override
    public void actionPerformed(ActionEvent e) {
        int index = manager.getDisplayedList().getSelectedIndex();
        if (manager.getMedCab().getCabinet().get(index).getDosage()
                <= manager.getMedCab().getCabinet().get(index).getNumPillsRemaining()) {
            if (!manager.getMedCab().getCabinet().get(index).getMediTimer().isReadyToTake()) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to take"
                                + " this medication? It has not yet been a full " + manager.getMedCab().getCabinet()
                                .get(index).getDosage() + " hours since your last consumption.",
                        "It is not yet time to take this medication!", JOptionPane.YES_NO_CANCEL_OPTION);
                if (result != 0) {
                    return;
                }
            }
            manager.getMedCab().getCabinet().get(index).eatPill();
            manager.informDetailWindow();
            manager.revalidate();
            manager.repaint();
        } else {
            JOptionPane.showMessageDialog(null, "You don't have enough pills to take one"
                    + " full dose.", "Cannot take pill", JOptionPane.ERROR_MESSAGE);
        }
    }
}
