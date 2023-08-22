package ui.action;

import ui.MediManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class DecreasePillAction extends AbstractAction {
    private MediManagerGUI manager;

    // EFFECTS: clearMed action constructor
    public DecreasePillAction(MediManagerGUI manager) {
        super();
        this.manager = manager;
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: decrements pills by 1
    @Override
    public void actionPerformed(ActionEvent e) {
        manager.getMedCab().getCabinet().get(manager.getDisplayedList().getSelectedIndex()).removeOnePill();
        manager.informDetailWindow();
        manager.repaint();
        manager.revalidate();
    }
}
