package ui.action;

import ui.MediManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class IncreasePillAction extends AbstractAction {
    private MediManagerGUI manager;

    // EFFECTS: clearMed action constructor
    public IncreasePillAction(MediManagerGUI manager) {
        super();
        this.manager = manager;
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: increments pills by 1
    @Override
    public void actionPerformed(ActionEvent e) {
        manager.getMedCab().getCabinet().get(manager.getDisplayedList().getSelectedIndex()).addOnePill();
        manager.informDetailWindow();
        manager.repaint();
        manager.revalidate();
    }
}
