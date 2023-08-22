package ui.action;

import ui.MediManagerGUI;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Timer;
import java.util.TimerTask;

public class ClickMedAction implements ListSelectionListener {
    MediManagerGUI manager;

    // EFFECTS: clickMed action constructor
    public ClickMedAction(MediManagerGUI manager) {
        this.manager = manager;
    }


    // MODIFIES: this, MediManagerGUI
    // EFFECTS: dictates what happens when an item is selected in JList
    @Override
    public void valueChanged(ListSelectionEvent e) {
        manager.informDetailWindow();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (manager.getDisplayedList().getSelectedIndex() > -1) {
                    manager.displayTimeRemaining(manager.getMedCab().getCabinet()
                            .get(manager.getDisplayedList().getSelectedIndex()));
                    manager.repaint();
                    manager.revalidate();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
}
