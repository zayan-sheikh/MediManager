package ui.action;

import ui.MediManagerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

// represents the action to be taken when a user wants to add a new medication.
public class DarkModeAction extends AbstractAction {
    private final Color darkBack = MediManagerGUI.mainGray;
    private final Color darkFore = MediManagerGUI.mainLightGrey;
    private final Color lightBack = new Color(0xFAFAFA);
    private final Color lightFore = new Color(0xE8E8E8);
    private final Color detailLightText = new Color(0xFF5800);
    private final Color detailDarkText = MediManagerGUI.mainOrange;
    private MediManagerGUI manager;
    private JToggleButton button;

    public DarkModeAction(MediManagerGUI manager, JToggleButton button) {
        super("");
        this.manager = manager;
        this.button = button;
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: determines action to take for dark or light mode depending on if
    // the dark mode button is on or off
    @Override
    public void actionPerformed(ActionEvent e) {
        if (button.isSelected()) {
            manager.getDmButton().setIcon(manager.moonIcon);
            repaintBackground(lightBack);
            repaintForeground(lightFore);
            repaintFonts(darkBack);
            repaintTimer(darkBack,new Color(0x068616));
            repaintButtons(new Color(0xE3E3E3));
            repaintDetailButtons("light");

        } else {
            manager.getDmButton().setIcon(manager.sunIcon);
            repaintBackground(darkBack);
            repaintForeground(darkFore);
            repaintFonts(new Color(0xFFFFFF));
            repaintTimer(new Color(0xFFFFFF),new Color(0x56FF7B));
            repaintButtons(darkBack);
            repaintDetailButtons("dark");
        }
        manager.repaint();
        manager.revalidate();
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: repaints buttons in the details button panel
    private void repaintDetailButtons(String input) {
        if (input.equals("light")) {
            manager.getRemoveButton().setForeground(new Color(0xFF3C3C));
            manager.getIncreaseRefillNumButton().setForeground(detailLightText);
            manager.getDecreaseRefillNumButton().setForeground(detailLightText);
            manager.getDecreasePillNumButton().setForeground(detailLightText);
            manager.getIncreasePillNumButton().setForeground(detailLightText);
            manager.getTakePillButton().setForeground(detailLightText);
        } else {
            manager.getRemoveButton().setForeground(new Color(0xFF704F));
            manager.getIncreaseRefillNumButton().setForeground(detailDarkText);
            manager.getDecreaseRefillNumButton().setForeground(detailDarkText);
            manager.getDecreasePillNumButton().setForeground(detailDarkText);
            manager.getIncreasePillNumButton().setForeground(detailDarkText);
            manager.getTakePillButton().setForeground(detailDarkText);

        }
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: recolours buttons
    private void repaintButtons(Color c) {
        manager.getIncreaseRefillNumButton().setBackground(c);
        manager.getDecreaseRefillNumButton().setBackground(c);
        manager.getRemoveButton().setBackground(c);
        manager.getTakePillButton().setBackground(c);
        manager.getIncreasePillNumButton().setBackground(c);
        manager.getDecreasePillNumButton().setBackground(c);
        manager.getDetailsButtonTopRow().setBackground(c);
        manager.getDetailsButtonBotRow().setBackground(c);
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: repaints timer font in GUI
    private void repaintTimer(Color timer, Color timeup) {
        int index = manager.getDisplayedList().getSelectedIndex();
        if (index > -1) {
            if (manager.getMedCab().getCabinet().get(index).getMediTimer().getNextTime().after(new Date())) {
                manager.getTimeLeft().setForeground(timer);
            } else {
                manager.getTimeLeft().setForeground(timeup);
            }
        }
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: repaints most fonts in GUI
    private void repaintFonts(Color c) {
        manager.getDisplayedList().setForeground(c);
        manager.getNameText().setForeground(c);
        manager.getFoodText().setForeground(c);
        manager.getDoseText().setForeground(c);
        manager.getPillNumText().setForeground(c);
        manager.getRefillNumText().setForeground(c);
        manager.getDisplayedList().setSelectionForeground(c);
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: repaints foreground
    private void repaintForeground(Color c) {
        manager.getDetailWindow().setBackground(c);
        manager.getRightButtonPanel().setBackground(c);
        manager.getDisplayedList().setSelectionBackground(c);
    }

    // MODIFIES: this, MediManagerGUI
    // EFFECTS: repaints background
    private void repaintBackground(Color c) {
        manager.getContentPanel().setBackground(c);
        manager.getDisplayedList().setBackground(c);
        manager.getListPanel().setBackground(c);
        manager.getRightSidebar().setBackground(c);
        manager.getDmButton().setBackground(c);
        manager.getDarkLightPanel().setBackground(c);
        manager.getDetailsDarkLightPanel().setBackground(c);

    }
}
