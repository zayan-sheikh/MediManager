package ui.action;

import ui.MediManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class DecreaseRefillAction extends AbstractAction {
    private MediManagerGUI manager;

    // EFFECTS: clearMed action constructor
    public DecreaseRefillAction(MediManagerGUI manager) {
        super();
        this.manager = manager;
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: decrements refills by 1
    @Override
    public void actionPerformed(ActionEvent e) {
        manager.getMedCab().getCabinet().get(manager.getDisplayedList().getSelectedIndex()).removeOneRefill();
        manager.informDetailWindow();
        manager.repaint();
        manager.revalidate();
    }
}
