package ui.action;

import model.Medication;
import ui.MediManagerGUI;
import ui.exception.CancelException;

import javax.swing.*;
import java.awt.event.ActionEvent;

// represents the action to be taken when a user wants to add a new medication.
public class AddMedAction extends AbstractAction {

    MediManagerGUI manager;
    boolean foodBool = false;
    int dose = 0;
    double freq = 0;
    int numPills = 0;
    int numRefills = 0;
    String name = null;

    // EFFECTS: constructor for AddMed action
    public AddMedAction(MediManagerGUI manager) {
        super("ADD MEDICATION");
        this.manager = manager;
    }

    // MODIFIES: medCab, displayList, medsModel
    // EFFECTS: Upon button press, gets user to construct a medication and adds it to the cabinet.
    @Override
        public void actionPerformed(ActionEvent event) {
        try {
            name = recordName();
            dose = recordDosage();
            freq = recordFrequency();
        } catch (CancelException ignored) {
            System.out.println("Action cancelled.");
            return;
        }
        if (checkFoodResponse()) {
            return;
        }
        try {
            recordPillQuantities();
        } catch (CancelException e) {
            System.out.println("Action cancelled.");
            return;
        }
        outputResults();
        manager.informDetailWindow();
    }

    // EFFECTS: checks response from user for food state of medicine
    private boolean checkFoodResponse() {
        int foodResult = JOptionPane.showConfirmDialog(null,
                "Do you need to take this pill with food?");
        if (0 == foodResult) {
            foodBool = true;
        } else if (2 == foodResult) {
            System.out.println("Action cancelled.");
            return true;
        }
        return false;
    }

    // EFFECTS: Mainly to reduce line count in actionPerformed() method for checkstyle; simply gets pill num and
    // refill num from user input
    private void recordPillQuantities() throws CancelException {
        numPills = recordPillNum();
        numRefills = recordRefillNum();
    }

    // MODIFIES: medCab, displayList, medsModel
    // EFFECTS: Outputs the results of the button press to lists used for GUI
    private void outputResults() {
        manager.getMedCab().addMedToCab(new Medication(name, dose, freq, foodBool, numPills, numRefills));
        manager.getMedsModel().addElement(name);
        manager.getDisplayedList().setModel(manager.getMedsModel());

    }

    // EFFECTS: records user input for refill number
    private int recordRefillNum() throws CancelException {
        int num = -1;
        while (num == -1) {
            String input = JOptionPane.showInputDialog(null,
                        "How many refills do you have? (If none, enter 0)",
                        "Adding medication...", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                throw new CancelException();
            }
            num = getIntegerInput(input);
            if (num == -1) {
                JOptionPane.showMessageDialog(null,
                        "Not valid, please enter a whole number greater than or equal to zero!",
                        "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
            }
        }
        return num;
    }

    // EFFECTS: records user input for pill number
    private int recordPillNum() throws CancelException {
        int num = -1;
        while (num == -1) {
            String input = JOptionPane.showInputDialog(null,
                    "How many pills do you currently have?",
                    "Adding medication...", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                throw new CancelException();
            }
            num = getIntegerInput(input);
            if (num == -1) {
                JOptionPane.showMessageDialog(null,
                            "Not valid, please enter a whole number greater than or equal to zero!",
                            "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
            }
        }
        return num;
    }

    // EFFECTS: returns inputted frequency from user, returning -1.0 if anything other than a positive/0 double is
    // inputted
    private double recordFrequency() throws CancelException {
        double freq = -1.0;
        while (freq == -1.0) {
            String input = JOptionPane.showInputDialog(null,
                    "How often do you need to take the medication (in hours since last consumption)?",
                    "Adding medication...", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                throw new CancelException();
            }
            freq = getDoubleInput(input);
            if (freq == -1.0) {
                JOptionPane.showMessageDialog(null,
                        "Not valid, please enter any number greater than or equal to zero!",
                        "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
            }
        }
        return freq;
    }

    // EFFECTS: checks input from user, returns -1.0 if anything other than a positive/0 double is inputted
    private double getDoubleInput(String input) {
        double doubleInput = -1;
        if (input.matches("\\d+\\.?\\d*") && Double.parseDouble(input) >= 0) {
            doubleInput = Double.parseDouble(input);
        }
        return doubleInput;
    }

    // EFFECTS: returns inputted dosage from user, returning -1 if anything other than 0 or a positive integer is
    // inputted
    private Integer recordDosage() throws CancelException {
        int dose = -1;
        while (dose == -1) {
            String input = JOptionPane.showInputDialog(null,
                    "How many pills do you need to take at a time?",
                    "Adding medication...", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                throw new CancelException();
            }
            dose = getIntegerInput(input);
            if (dose == -1) {
                JOptionPane.showMessageDialog(null,
                        "Not valid, please enter a whole number greater than or equal to zero!",
                        "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
            }
        }
        return dose;
    }

    // EFFECTS: checks input from user, returns -1 if anything other than 0 or a positive integer is
    // inputted
    private int getIntegerInput(String input) {
        int integerInput = -1;
        if (input.matches("\\d+") && Integer.parseInt(input) >= 0) {
            integerInput = Integer.parseInt(input);
        }
        return integerInput;
    }

    // EFFECTS: records name from user input
    private String recordName() throws CancelException {
        String input = JOptionPane.showInputDialog(null, "What is the name of your Medication?",
                "Adding medication...", JOptionPane.QUESTION_MESSAGE);
        if (input == null) {
            throw new CancelException();
        }
        return input;
    }
}

