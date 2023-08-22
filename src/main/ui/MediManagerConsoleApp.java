package ui;

import model.Medication;
import model.MedicineCabinet;
import persistance.JsonReader;
import persistance.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

// Medication Manager application
public class MediManagerConsoleApp {
    public static final String JSON_STORE = "./data/cabinet.json";
    private MedicineCabinet manager;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs MediManager app
    public MediManagerConsoleApp() {
        try {
            runMediManager();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runMediManager() throws FileNotFoundException {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nMediManager closed!");
    }

    // EFFECTS: displays initial menu of options to user
    public void displayMenu() {
        System.out.println("Your current medications:");
        int count = 1;
        if (manager.getCabinet().size() == 0) {
            System.out.println("None currently added.");
        } else {
            for (Medication med : manager.getCabinet()) {
                System.out.println(count + ". " + med.getName());
                ++count;
            }
        }
        System.out.println("    ->(To select a medicine for more details, type its number)");
        System.out.println("    ->(To add new pill, type a)");
        System.out.println("    ->(To save current configuration, type s)");
        System.out.println("    ->(To load a saved configuration, type l)");
        System.out.println("    ->(Type q to quit)");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    // CITATION: one of the else if statements uses a regular expression, which was inspired from the following
    //           thread: https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
    private void processCommand(String command) {
        if (command.equals("a")) {
            doAddPill();
        } else if (command.matches("s")) {
            saveMedicineCabinet();
        } else if (command.matches("l")) {
            loadMedicineCabinet();
        } else if (command.matches("\\d+") && (Integer.parseInt(command) <= manager.getCabinet().size())) {
            manipulateMed(Integer.parseInt(command));
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: medication at (index - 1), manager
    // EFFECTS: creates a menu of details and operations for a specific medication
    private void manipulateMed(int index) {
        if (index <= 0) {
            System.out.println("Selection not valid...");
        } else {
            int sizeList = manager.getCabinet().size();
            getMedMenu(index, sizeList);
        }
    }

    // EFFECTS: Sets up and displays entire interface for one medication
    private void getMedMenu(int index, int sizeList) {
        while (sizeList == manager.getCabinet().size()) {
            printDetailedMenu(index);
            String command = input.next();
            command = command.toLowerCase();
            if (command.equals("r")) {
                manager.getCabinet().remove(index - 1);
            } else if (command.equals("b")) {
                break;
            } else if (command.equals("t")) {
                manager.getCabinet().get(index - 1).eatPill();
            } else if (command.equals("+")) {
                manager.getCabinet().get(index - 1).addOnePill();
            } else if (command.equals("-")) {
                manager.getCabinet().get(index - 1).removeOnePill();
            } else if (command.equals("i")) {
                manager.getCabinet().get(index - 1).addOneRefill();
            } else if (command.equals("d")) {
                manager.getCabinet().get(index - 1).removeOneRefill();
            }
        }
    }

    // EFFECTS: prints the text displayed in the detailed medication menu
    private void printDetailedMenu(int index) {
        System.out.println(manager.getCabinet().get(index - 1).getName() + ":");
        System.out.println("-Take " + manager.getCabinet().get(index - 1).getDosage() + " pill"
                + pluralize(manager.getCabinet().get(index - 1).getDosage()) + " per dose.");
        System.out.println("-" + printFoodState(manager.getCabinet().get(index - 1).takeWithFood()));
        System.out.println("-You have " + manager.getCabinet().get(index - 1).getNumPillsRemaining()
                + " pill" + pluralize(manager.getCabinet().get(index - 1).getNumPillsRemaining()) + " remaining.");
        System.out.println("-You have " + manager.getCabinet().get(index - 1).getNumRefillsRemaining()
                + " refill" + pluralize(manager.getCabinet().get(index - 1).getNumRefillsRemaining()) + " remaining.");
        System.out.println("-" + takePrint(manager.getCabinet().get(index - 1)));

        System.out.println("    ->(Type t to take one dose. [will also reset dosage timing tracker])");
        System.out.println("    ->(Type + to add one pill.)");
        System.out.println("    ->(Type - to remove one pill.)");
        System.out.println("    ->(Type i to add one refill.)");
        System.out.println("    ->(Type d to remove one refill.)");
        System.out.println("    ->(Type r to remove this medication entirely.)");
        System.out.println("    ->(Type b to go back to the menu.)");
    }

    // EFFECTS: Generates text for whether it is time to take a medication or not, and updates isReadyToTake
    private String takePrint(Medication med) {
        Date currentTime = new Date();
        if (med.getMediTimer().isReadyToTake() | !med.getMediTimer().getNextTime().after(currentTime)) {
            med.getMediTimer().setReadyToTake(true);
            return "It is time to take this medication, as it has been at least " + med.getMediTimer().getFrequency()
                    + " hour" + pluralize(med.getMediTimer().getFrequency()) + " since your" + " last dose!";
        } else {
            return "It is NOT yet time to take this medication as it has not been "
                    + med.getMediTimer().getFrequency() + " hour" + pluralize(med.getMediTimer().getFrequency())
                    + " since your last dose. " + med.getMediTimer().getDifferenceTime();
        }
    }

    // EFFECTS: pluralizes words if there is more (or less) than 1 quantity
    public static String pluralize(double quantity) {
        if (quantity != 1) {
            return "s";
        } else {
            return "";
        }
    }

    //EFFECTS: aids in manipulateMed() to declare whether medicine should be taken with food.
    public static String printFoodState(boolean b) {
        if (b) {
            return "You must take this pill with food.";
        } else {
            return "This pill is not to be taken with food.";
        }
    }

    // EFFECTS: creates new medication from user input
    private void doAddPill() {
        String nme;
        int dose;
        double freq;
        boolean foodBool;
        int numPills;
        int numRefills;

        System.out.println("What is the name of your medication?");
        nme = input.next();

        System.out.println("How many pills do you need to take at a time?");
        dose = getIntInput();

        System.out.println("How often do you need to take the medication (in hours since last consumption)");
        freq = getDoubleInput();

        System.out.println("Do you need to take this pill with food? (yes/no)");
        foodBool = yesNo();

        System.out.println("How many pills do you have?");
        numPills = getIntInput();

        System.out.println("How many refills do you have for your pills? (if none, enter 0)");
        numRefills = getIntInput();

        Medication newMed = new Medication(nme, dose, freq, foodBool, numPills, numRefills);
        manager.addMedToCab(newMed);
    }

    // EFFECTS: gets double user input. If given something other than a positive real number, asks them to retry.
    private double getDoubleInput() {
        double userInput = -1;
        while (userInput < 0) {
            String command = input.next();
            if (command.matches("\\d+\\.?\\d*")) {
                userInput = Double.parseDouble(command);
            } else {
                System.out.println("Not valid, please enter a number greater than or equal to zero!");
            }
        }
        return userInput;
    }

    // EFFECTS: gets integer user input. If given something other than a positive integer, asks them to retry.
    private int getIntInput() {
        int userInput = -1;
        while (userInput < 0) {
            String command = input.next();
            if (command.matches("\\d+")) {
                userInput = Integer.parseInt(command);
            } else {
                System.out.println("Not valid, please enter a whole number greater than or equal to zero!");
            }
        }
        return userInput;
    }

    // EFFECTS: Converts the words yes and no to booleans, prompts a retry if another string is entered
    private boolean yesNo() {
        boolean foodBool = false;
        String s;
        do {
            s = input.next();
            s = s.toLowerCase();
            if (s.equals("yes")) {
                foodBool = true;
            } else {
                System.out.println("Not valid, please try again and only enter either 'yes' or 'no'");
            }
        } while (!(s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("no")));
        return foodBool;
    }

    // MODIFIES: this
    // EFFECTS: initializes medicine cabinet
    private void init() {
        this.manager = new MedicineCabinet();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: saves the MedicineCabinet manager to file
    private void saveMedicineCabinet() {
        try {
            jsonWriter.open();
            jsonWriter.write(manager);
            jsonWriter.close();
            System.out.println("Saved current MediManager configuration to: " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads MedicineCabinet from file
    private void loadMedicineCabinet() {
        try {
            manager = jsonReader.read();
            System.out.println("Loaded your MediManager configuration from: " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: main method that runs console app
    public static void main(String[] args) {
        new MediManagerConsoleApp();
    }
}
