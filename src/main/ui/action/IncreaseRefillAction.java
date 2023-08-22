package ui.action;

import ui.MediManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class IncreaseRefillAction extends AbstractAction {
    private MediManagerGUI manager;

    // EFFECTS: clearMed action constructor
    public IncreaseRefillAction(MediManagerGUI manager) {
        super();
        this.manager = manager;
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: increments refills by 1
    @Override
    public void actionPerformed(ActionEvent e) {
        manager.getMedCab().getCabinet().get(manager.getDisplayedList().getSelectedIndex()).addOneRefill();
        manager.informDetailWindow();
        manager.repaint();
        manager.revalidate();
    }
}
