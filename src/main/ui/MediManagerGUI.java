package ui;

import model.*;
import model.Event;
import persistance.*;

import ui.action.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static ui.MediManagerConsoleApp.*;

public class MediManagerGUI extends JFrame {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int DRAGGABLE_TOP_SIZE = 20;

    public static final Color mainOrange = new Color(-29879);
    public static final Color mainGray = new Color(-14869475);
    public static final Color mainLightGrey = new Color(0x282828);

    private Font font;
    private Font buttonFont;
    private Font displayedListFont;
    private Font displayedListFontUnderlined;
    private Font listFont;
    private Font listFontUnderlined;
    private Font detailFontSmall;
    private Font smallButtonFont;

    private final ImageIcon splashGif = new ImageIcon("data/resources/Color Matte.gif");
    private final ImageIcon logo = new ImageIcon("data/resources/MediManagerIcon.png");
    private final ImageIcon addIcon = new ImageIcon("data/resources/add.png");
    private final ImageIcon removeIcon = new ImageIcon("data/resources/remove.png");
    public final ImageIcon sunIcon = new ImageIcon("data/resources/sun.png");
    public final ImageIcon moonIcon = new ImageIcon("data/resources/moon.png");
    public final ImageIcon spacer = new ImageIcon("data/resources/spacer.png");
    private MedicineCabinet medCab = new MedicineCabinet();
    private DefaultListModel<String> medsModel = new DefaultListModel<>();
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    protected JPanel contentPanel;
    private JWindow splashScreen;
    private JPanel listPanel;
    private JPanel leftSidebar;
    private JPanel rightSidebar;
    private JPanel detailWindow;
    private JLabel managerText;
    private JLabel listTitle;
    private JLabel detailsText;
    private JPanel sidebarButtonPanelLeft;
    private JButton loadButton;
    private JButton addButton;
    private JButton saveButton;
    private JList displayedList;
    private JButton clearButton;
    private JButton removeButton;
    private JButton takePillButton;
    private JButton increasePillNumButton;
    private JButton decreasePillNumButton;
    private JButton increaseRefillNumButton;
    private JButton decreaseRefillNumButton;
    ImageIcon icon = new ImageIcon("data/resources/MediManagerIcon.png");
    private JLabel timeLeft = new JLabel();
    private JPanel rightButtonPanel;
    private JLabel nameText = new JLabel();
    private JToggleButton dmButton;
    private JPanel darkLightPanel;
    private JLabel refillNumText = new JLabel();
    private JLabel doseText = new JLabel();
    private JLabel foodText = new JLabel();
    private JLabel pillNumText = new JLabel();
    private JPanel detailsButtonTopRow;
    private JPanel detailsButtonBotRow;
    private JPanel logoPanel;

    public JPanel getDarkLightPanel() {
        return darkLightPanel;
    }

    public void setDarkLightPanel(JPanel darkLightPanel) {
        this.darkLightPanel = darkLightPanel;
    }

    public JPanel getDetailsDarkLightPanel() {
        return detailsDarkLightPanel;
    }

    public void setDetailsDarkLightPanel(JPanel detailsDarkLightPanel) {
        this.detailsDarkLightPanel = detailsDarkLightPanel;
    }

    private JPanel detailsDarkLightPanel;

    public MediManagerGUI() {
        fontCreate();
        init();
        leftSidebarSetup();
        listSetup();
        rightSidebarSetup();
    }

    // EFFECTS: creates the font to be used for the GUI
    private void fontCreate() {
        try {
            this.font = Font.createFont(Font.TRUETYPE_FONT,
                    new File("data/resources/CODE Bold.otf")).deriveFont(48f);
            this.displayedListFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("data/resources/Cardium A Regular.otf")).deriveFont(20f);
            this.detailFontSmall = Font.createFont(Font.TRUETYPE_FONT,
                    new File("data/resources/Cardium A Regular.otf")).deriveFont(17f);
            this.buttonFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("data/resources/CODE Bold.otf")).deriveFont(18f);
            this.smallButtonFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("data/resources/CODE Bold.otf")).deriveFont(13f);
            this.listFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("data/resources/CODE Bold.otf")).deriveFont(40f);
            Map attributes = listFont.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_TWO_PIXEL);
            this.listFontUnderlined = listFont.deriveFont(attributes);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(detailFontSmall);

            Map attributesDisplayList = displayedListFont.getAttributes();
            attributesDisplayList.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            this.displayedListFontUnderlined = displayedListFont.deriveFont(attributesDisplayList);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes list of medications
    private void listSetup() {
        listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(new Color(0x1D1C1D));
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBackground(new Color(-13355981));
        scrollPane.setForeground(new Color(-16184819));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        displayedList = new JList();
        displayedList.setBorder(new EmptyBorder(0, 20, 0, 0));
        displayedList.setBackground(mainGray);
        displayedList.setFont(displayedListFont);
        displayedList.setForeground(new Color(-1838338));
        displayedList.setSelectionBackground(mainLightGrey);
        displayedList.setSelectionForeground(new Color(-1));
        displayedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        displayedList.addListSelectionListener(new ClickMedAction(this));
        listPanel.add(scrollPane, BorderLayout.CENTER);
        createListTitle();
        contentPanel.add(listPanel);
        scrollPane.setViewportView(displayedList);
        revalidate();

    }

    // MODIFIES: this
    // EFFECTS: creates a title on the main JList
    private void createListTitle() {
        listTitle = new JLabel("Your Medications");
        listTitle.setFont(listFontUnderlined);
        listTitle.setForeground(new Color(0xFF8B49));
        listTitle.setMaximumSize(new Dimension(320, 90));
        listTitle.setMinimumSize(new Dimension(320, 90));
        listTitle.setPreferredSize(new Dimension(320, 90));
        listTitle.setVerticalAlignment(0);
        listTitle.setHorizontalAlignment(0);
        listTitle.setVerticalTextPosition(0);
        listTitle.setAlignmentX(CENTER_ALIGNMENT);
        listPanel.add(listTitle, BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS: sets up the right sidebar's graphics and buttons
    private void rightSidebarSetup() {
        rightSidebar = new JPanel();
        rightSidebar.setBorder(new EmptyBorder(0, 20, 20, 20));
        rightSidebar.setSize(200,-1);
        rightSidebar.setLayout(new BorderLayout());
        rightSidebar.setBackground(new Color(0x1D1C1D));
        rightSidebar.setForeground(mainOrange);
        detailsDarkLightPanel = new JPanel(new BorderLayout());
        createAboveDetailText();
        detailWindow = new JPanel(new GridLayout(7,1));
        detailWindow.setBackground(mainLightGrey);
        rightSidebar.add(detailWindow);
        createRightButtonPanel();
        informDetailWindow();
        nameText.setForeground(new Color(0xFFFFFF));
        pillNumText.setForeground(new Color(0xFFFFFF));
        refillNumText.setForeground(new Color(0xFFFFFF));
        foodText.setForeground(new Color(0xFFFFFF));
        doseText.setForeground(new Color(0xFFFFFF));
        timeLeft.setForeground(new Color(0xFFFFFF));
        nameText.setForeground(new Color(0xFFFFFF));

        revalidate();


    }

    // MODIFIES: this
    // EFFECTS: creates details text and dark mode toggle button
    private void createAboveDetailText() {
        detailsDarkLightPanel.setBackground(mainGray);
        contentPanel.add(rightSidebar,2);
        rightSidebar.add(detailsDarkLightPanel, BorderLayout.NORTH);
        createDetailsText();
        darkLightPanel = new JPanel(new GridLayout(1,25));
        darkLightPanel.setBorder(new EmptyBorder(0,0,0,5));
        darkLightPanel.setBackground(mainGray);
        detailsDarkLightPanel.add(darkLightPanel,BorderLayout.EAST);
        createDarkModeButton();
    }

    // MODIFIES: this
    // EFFECTS: sets up right button panel
    private void createRightButtonPanel() {
        rightButtonPanel = new JPanel(new GridLayout(2,1));
        rightButtonPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
        rightButtonPanel.setBackground(mainLightGrey);

        detailsButtonTopRow = new JPanel(new GridLayout(1,4));
        detailsButtonTopRow.setBackground(mainGray);

        createIncreasePillButton();
        createDecreasePillButton();
        createIncreaseRefillButton();
        createDecreaseRefillButton();

        detailsButtonTopRow.add(increasePillNumButton);
        detailsButtonTopRow.add(decreasePillNumButton);

        detailsButtonTopRow.add(increaseRefillNumButton);
        detailsButtonTopRow.add(decreaseRefillNumButton);

        detailsButtonBotRow = new JPanel(new GridLayout(1,2));
        detailsButtonBotRow.setBackground(mainGray);
        createTakePillButton();
        createRemoveButton();

        detailsButtonBotRow.add(takePillButton);
        detailsButtonBotRow.add(removeButton);



        rightButtonPanel.add(detailsButtonTopRow);
        rightButtonPanel.add(detailsButtonBotRow);

    }

    // MODIFIES: this
    // EFFECTS: creates decrease refill button
    private void createDecreaseRefillButton() {
        decreaseRefillNumButton = new JButton();
        decreaseRefillNumButton.setActionCommand("");
        decreaseRefillNumButton.setBackground(mainGray);
        decreaseRefillNumButton.setBorderPainted(false);
        decreaseRefillNumButton.setContentAreaFilled(true);
        decreaseRefillNumButton.setDefaultCapable(true);
        decreaseRefillNumButton.setDoubleBuffered(false);
        decreaseRefillNumButton.setFocusable(false);
        decreaseRefillNumButton.setAction(new DecreaseRefillAction(this));
        decreaseRefillNumButton.setFont(smallButtonFont);
        decreaseRefillNumButton.setForeground(mainOrange);
        decreaseRefillNumButton.setIcon(removeIcon);
        decreaseRefillNumButton.setLabel("Refills");
        decreaseRefillNumButton.setOpaque(true);
        decreaseRefillNumButton.setSelected(false);
        decreaseRefillNumButton.setText("Refills");
    }

    // MODIFIES: this
    // EFFECTS: creates increase refill button
    private void createIncreaseRefillButton() {
        increaseRefillNumButton = new JButton();
        increaseRefillNumButton.setActionCommand("");
        increaseRefillNumButton.setBackground(mainGray);
        increaseRefillNumButton.setBorderPainted(false);
        increaseRefillNumButton.setContentAreaFilled(true);
        increaseRefillNumButton.setDefaultCapable(true);
        increaseRefillNumButton.setDoubleBuffered(false);
        increaseRefillNumButton.setFocusable(false);
        increaseRefillNumButton.setAction(new IncreaseRefillAction(this));
        increaseRefillNumButton.setFont(smallButtonFont);
        increaseRefillNumButton.setForeground(mainOrange);
        increaseRefillNumButton.setIcon(addIcon);
        increaseRefillNumButton.setLabel("Refills");
        increaseRefillNumButton.setOpaque(true);
        increaseRefillNumButton.setSelected(false);
        increaseRefillNumButton.setText("Refills");
    }

    // MODIFIES: this
    // EFFECTS: creates decrease pill button
    private void createDecreasePillButton() {
        decreasePillNumButton = new JButton();
        decreasePillNumButton.setActionCommand("");
        decreasePillNumButton.setBackground(mainGray);
        decreasePillNumButton.setBorderPainted(false);
        decreasePillNumButton.setContentAreaFilled(true);
        decreasePillNumButton.setDefaultCapable(true);
        decreasePillNumButton.setDoubleBuffered(false);
        decreasePillNumButton.setFocusable(false);
        decreasePillNumButton.setAction(new DecreasePillAction(this));
        decreasePillNumButton.setFont(smallButtonFont);
        decreasePillNumButton.setForeground(mainOrange);
        decreasePillNumButton.setIcon(removeIcon);
        decreasePillNumButton.setLabel("Refills");
        decreasePillNumButton.setOpaque(true);
        decreasePillNumButton.setSelected(false);
        decreasePillNumButton.setText("Pills");
    }

    // MODIFIES: this
    // EFFECTS: creates increase pill button
    private void createIncreasePillButton() {
        increasePillNumButton = new JButton();
        increasePillNumButton.setActionCommand("");
        increasePillNumButton.setBackground(mainGray);
        increasePillNumButton.setBorderPainted(false);
        increasePillNumButton.setContentAreaFilled(true);
        increasePillNumButton.setDefaultCapable(true);
        increasePillNumButton.setDoubleBuffered(false);
        increasePillNumButton.setFocusable(false);
        increasePillNumButton.setAction(new IncreasePillAction(this));
        increasePillNumButton.setFont(smallButtonFont);
        increasePillNumButton.setIcon(addIcon);
        increasePillNumButton.setForeground(mainOrange);
        increasePillNumButton.setLabel("Refills");
        increasePillNumButton.setOpaque(true);
        increasePillNumButton.setSelected(false);
        increasePillNumButton.setText("Pills");
    }

    // MODIFIES: this
    // EFFECTS: chooses text for detail window depending on current user state
    public void informDetailWindow() {
        if (medCab.getCabinet().size() == 0) {
            detailWindow.removeAll();
            emptyCabinetPrompt();
        } else if (displayedList.getSelectedIndex() == -1) {
            detailWindow.removeAll();
            notSelectedPrompt();
        } else {
            detailWindow.removeAll();
            produceDetails();
            repaint();
        }
        revalidate();
    }

    // EFFECTS: creates text for detail window when selected
    private void produceDetails() {
        Medication med = medCab.getCabinet().get(displayedList.getSelectedIndex());
        displayMedName(med,nameText);

        doseText.setText("Take " + med.getDosage() + " pill" + pluralize(med.getDosage())
                + " per dose.");
        configureDetailText(doseText);

        foodText.setText(printFoodState(med.takeWithFood()));
        configureDetailText(foodText);

        pillNumText.setText("You have " + med.getNumPillsRemaining() + " pill"
                + pluralize(med.getNumPillsRemaining()) + " remaining.");
        configureDetailText(pillNumText);

        refillNumText.setText(("You have " + med.getNumRefillsRemaining() + " refill"
                + pluralize(med.getNumRefillsRemaining()) + " remaining."));
        configureDetailText(refillNumText);

        configureDetailText(timeLeft);
        displayTimeRemaining(med);

        detailWindow.add(nameText);
        detailWindow.add(doseText);
        detailWindow.add(foodText);
        detailWindow.add(pillNumText);
        detailWindow.add(refillNumText);
        detailWindow.add(timeLeft);
        detailWindow.add(rightButtonPanel);
    }

    // MODIFIES: this
    // EFFECTS: styles the text for medicine name in details window
    private void displayMedName(Medication med, JLabel nameText) {
        nameText.setText(med.getName());
        nameText.setFont(displayedListFontUnderlined);
        nameText.setHorizontalAlignment(SwingConstants.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: determines the timer message in details window
    public void displayTimeRemaining(Medication med) {
        Date currentTime = new Date();
        if (med.getMediTimer().isReadyToTake() | !med.getMediTimer().getNextTime().after(currentTime)) {
            med.getMediTimer().setReadyToTake(true);
            timeLeft.setText("It is time to take this medication.");
            if (dmButton.isSelected()) {
                timeLeft.setForeground(new Color(0x068616));
            } else {
                timeLeft.setForeground(new Color(0x56FF7B));
            }

        } else {
            if (dmButton.isSelected()) {
                timeLeft.setForeground(new Color(0x1D1C1D));
            } else {
                timeLeft.setForeground(new Color(0xFFFFFF));
            }

            timeLeft.setText("<html><p style=\"width:240px\"><center>" + med.getMediTimer().getDifferenceTime()
                    + "</center></p></html>");
        }
    }

    // MODIFIES: detailsText
    // EFFECTS: gives detailsText the desired look in the details window
    private void configureDetailText(JLabel detailsText) {
        detailsText.setFont(detailFontSmall);
        detailsText.setHorizontalAlignment(SwingConstants.CENTER);

    }

    // EFFECTS: produces text for details when no selected med
    private void notSelectedPrompt() {
        JLabel pretext = new JLabel("Select a medication for more information!");
        pretext.setFont(detailFontSmall);
        pretext.setHorizontalAlignment(SwingConstants.CENTER);
        pretext.setForeground(new Color(0x525252));
        JLabel spacer1 = new JLabel("");
        JLabel spacer2 = new JLabel("");
        JLabel spacer3 = new JLabel("");
        JLabel spacer4 = new JLabel("");
        JLabel spacer5 = new JLabel("");
        JLabel spacer6 = new JLabel("");

        detailWindow.add(spacer1);
        detailWindow.add(spacer2);
        detailWindow.add(spacer3);

        detailWindow.add(pretext);

        detailWindow.add(spacer4);
        detailWindow.add(spacer5);
        detailWindow.add(spacer6);
    }

    // EFFECTS: produces text for details when cabinet is empty
    private void emptyCabinetPrompt() {
        JLabel pretext = new JLabel("Add or load a medication to begin!");
        pretext.setFont(detailFontSmall);

        pretext.setHorizontalAlignment(SwingConstants.CENTER);
        pretext.setForeground(new Color(0x525252));
        JLabel spacer1 = new JLabel("");
        JLabel spacer2 = new JLabel("");
        JLabel spacer3 = new JLabel("");
        JLabel spacer4 = new JLabel("");
        JLabel spacer5 = new JLabel("");
        JLabel spacer6 = new JLabel("");

        detailWindow.add(spacer1);
        detailWindow.add(spacer2);
        detailWindow.add(spacer3);

        detailWindow.add(pretext);

        detailWindow.add(spacer4);
        detailWindow.add(spacer5);
        detailWindow.add(spacer6);
    }

    // MODIFIES: this
    // EFFECTS: creates the take pill button
    private void createTakePillButton() {
        takePillButton = new JButton();
        takePillButton.setActionCommand("");
        takePillButton.setBackground(mainGray);
        takePillButton.setBorderPainted(false);
        takePillButton.setContentAreaFilled(true);
        takePillButton.setDefaultCapable(true);
        takePillButton.setDoubleBuffered(false);
        takePillButton.setFocusable(false);
        takePillButton.setFont(smallButtonFont);
        takePillButton.setForeground(mainOrange);
        takePillButton.setLabel("Take Medication");
        takePillButton.setOpaque(true);
        takePillButton.setSelected(false);
        takePillButton.setText("Take Medication");
        takePillButton.setAction(new TakeAction(this));

    }

    // MODIFIES: this
    // EFFECTS: creates remove button
    private void createRemoveButton() {
        removeButton = new JButton();
        removeButton.setActionCommand("");
        removeButton.setBackground(mainGray);
        removeButton.setBorderPainted(false);
        removeButton.setContentAreaFilled(true);
        removeButton.setDefaultCapable(true);
        removeButton.setDoubleBuffered(false);
        removeButton.setFocusable(false);
        removeButton.setFont(smallButtonFont);
        removeButton.setForeground(new Color(0xFF704F));
        removeButton.setLabel("REMOVE MEDICATION");
        removeButton.setOpaque(true);
        removeButton.setSelected(false);
        removeButton.setText("REMOVE MEDICATION");
        removeButton.setAction(new RemoveMedAction(this));
    }

    // MODIFIES: this
    // EFFECTS: initializes and creates the options JLabel
    private void createDetailsText() {
        detailsText = new JLabel("details");
        detailsText.setFont(listFontUnderlined);
        detailsText.setForeground(mainOrange);
        detailsText.setMaximumSize(new Dimension(320, 90));
        detailsText.setMinimumSize(new Dimension(320, 90));
        detailsText.setPreferredSize(new Dimension(320, 90));
        detailsText.setVerticalAlignment(0);
        detailsText.setHorizontalAlignment(SwingConstants.CENTER);
        detailsText.setVerticalTextPosition(0);
        detailsText.setAlignmentX(CENTER_ALIGNMENT);
        detailsDarkLightPanel.add(detailsText, BorderLayout.CENTER);
        JButton spacerButton = new JButton();
        spacerButton.setBackground(mainGray);
        spacerButton.setBorderPainted(false);
        spacerButton.setContentAreaFilled(false);
        spacerButton.setFocusable(false);
        spacerButton.setFont(buttonFont);
        spacerButton.setForeground(new Color(-1));
        spacerButton.setLabel("");
        detailsDarkLightPanel.add(spacerButton, BorderLayout.WEST);
        spacerButton.setIcon(spacer);
    }

    // MODIFIES: this
    // EFFECTS: sets up the sidebar's graphics and buttons
    private void leftSidebarSetup() {
        leftSidebar = new JPanel();
        leftSidebar.setLayout(new BorderLayout(0, 3));
        leftSidebar.setBackground(mainOrange);
        contentPanel.add(leftSidebar);
        makeButtonPanelLeft();
        leftSidebar.add(sidebarButtonPanelLeft, BorderLayout.CENTER);
        logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(mainOrange);
        makeMediManagerText();
        leftSidebar.add(logoPanel, BorderLayout.NORTH);
        logoPanel.add(managerText, BorderLayout.CENTER);


        revalidate();

    }

    // MODIFIES: this
    // EFFECTS: creates dark mode button
    private void createDarkModeButton() {
        dmButton = new JToggleButton();
        dmButton.setBackground(mainGray);
        dmButton.setBorderPainted(false);
        dmButton.setContentAreaFilled(false);
        dmButton.setFocusable(false);
        dmButton.setFont(buttonFont);
        dmButton.setForeground(new Color(-1));
        dmButton.setLabel("");
        dmButton.setOpaque(true);
        dmButton.setSelected(false);
        dmButton.setText("");
        dmButton.setAction(new DarkModeAction(this,dmButton));
        dmButton.setIcon(sunIcon);
        darkLightPanel.add(dmButton);
    }

    // MODIFIES: this
    // EFFECTS: constructs a button panel for the left side of the screen
    private void makeButtonPanelLeft() {
        sidebarButtonPanelLeft = new JPanel(new GridLayout(4,1));
        sidebarButtonPanelLeft.setBorder(new EmptyBorder(15,15,15,15));
        sidebarButtonPanelLeft.setBackground(mainOrange);
        sidebarButtonPanelLeft.setForeground(new Color(-1));
        sidebarButtonPanelLeft.setInheritsPopupMenu(false);
        sidebarButtonPanelLeft.setMaximumSize(new Dimension(2147483647, -1));
        sidebarButtonPanelLeft.setMinimumSize(new Dimension(166, -1));
        sidebarButtonPanelLeft.setPreferredSize(new Dimension(166, -1));
        createAddButton();
        createSaveButton();
        createLoadButton();
        createClearButton();
    }

    // MODIFIES: this
    // EFFECTS: creates the load button
    private void createLoadButton() {
        loadButton = new JButton();
        loadButton.setBackground(mainOrange);
        loadButton.setBorderPainted(false);
        loadButton.setContentAreaFilled(true);
        loadButton.setDefaultCapable(true);
        loadButton.setDoubleBuffered(false);
        loadButton.setFocusable(false);
        loadButton.setFont(buttonFont);
        loadButton.setForeground(new Color(-1));
        loadButton.setLabel("Load");
        loadButton.setOpaque(true);
        loadButton.setSelected(false);
        loadButton.setText("Load");
        loadButton.setAction(new LoadMedAction(this, jsonReader));
        sidebarButtonPanelLeft.add(loadButton);
    }

    // MODIFIES: this
    // EFFECTS: creates the save button
    private void createSaveButton() {
        saveButton = new JButton();
        saveButton.setBackground(mainOrange);
        saveButton.setBorderPainted(false);
        saveButton.setContentAreaFilled(true);
        saveButton.setDefaultCapable(true);
        saveButton.setDoubleBuffered(false);
        saveButton.setFocusable(false);
        saveButton.setFont(buttonFont);
        saveButton.setForeground(new Color(-1));
        saveButton.setLabel("Save");
        saveButton.setOpaque(true);
        saveButton.setSelected(false);
        saveButton.setText("Save");
        saveButton.setAction(new SaveMedAction(this, jsonWriter));
        sidebarButtonPanelLeft.add(saveButton);
    }

    // MODIFIES: this
    // EFFECTS: creates the clear button
    private void createClearButton() {
        clearButton = new JButton();
        clearButton.setBackground(new Color(-36785));
        clearButton.setBorderPainted(false);
        clearButton.setContentAreaFilled(true);
        clearButton.setDefaultCapable(true);
        clearButton.setDoubleBuffered(false);
        clearButton.setFocusable(false);
        clearButton.setFont(buttonFont);
        clearButton.setForeground(new Color(-1));
        clearButton.setLabel("Clear all medications");
        clearButton.setOpaque(true);
        clearButton.setSelected(false);
        clearButton.setText("Clear all medications");
        clearButton.setAction(new ClearMedAction(this));
        sidebarButtonPanelLeft.add(clearButton);
    }

    // MODIFIES: this
    // EFFECTS: creates the add button
    private void createAddButton() {
        addButton = new JButton();
        addButton.setBackground(mainOrange);
        addButton.setBorderPainted(false);
        addButton.setContentAreaFilled(true);
        addButton.setDefaultCapable(true);
        addButton.setDoubleBuffered(false);
        addButton.setFocusable(false);
        addButton.setFont(buttonFont);
        addButton.setForeground(new Color(-1));
        addButton.setLabel("Add medication");
        addButton.setOpaque(true);
        addButton.setSelected(false);
        addButton.setText("Add medication");
        addButton.setAction(new AddMedAction(this));
        sidebarButtonPanelLeft.add(addButton);
    }

    // MODIFIES: this
    // EFFECTS: creates medimanager text logo
    private void makeMediManagerText() {
        managerText = new JLabel("MEDIMANAGER");
        managerText.setFont(font);
        managerText.setForeground(new Color(0xFFFFFF));
        managerText.setForeground(new Color(-328449));
        managerText.setHorizontalAlignment(0);
        managerText.setHorizontalTextPosition(0);
        managerText.setIconTextGap(10);
        managerText.setMaximumSize(new Dimension(320, 90));
        managerText.setMinimumSize(new Dimension(320, 90));
        managerText.setPreferredSize(new Dimension(320, 90));
        managerText.setAlignmentY(CENTER_ALIGNMENT);
        managerText.setAlignmentX(CENTER_ALIGNMENT);
        ImageIcon logo = new ImageIcon("data/resources/mmiconblack.png");
        managerText.setIcon(logo);
        managerText.setHorizontalTextPosition(JLabel.RIGHT);
    }

    // MODIFIES: this
    // EFFECTS: Initialises MediManager window
    private void init() {
        initSplashScreen();
        contentPanel = new JPanel(new GridLayout(1,3));
        setTitle("MediManager");
        contentPanel.setBackground(mainGray);
        contentPanel.setForeground(mainGray);
        this.setIconImage(icon.getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setContentPane(contentPanel);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        Timer splashTime = new Timer();
        TimerTask splashClose = new TimerTask() {
            @Override
            public void run() {
                splashScreen.dispose();
                setVisible(true);
            }
        };
        splashTime.schedule(splashClose, 3000);
        printEventLog();
    }

    // MODIFIES: this
    // EFFECTS: prints event log
    private void printEventLog() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event.toString());
                }
                super.windowClosed(e);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: initializes splash screen
    private void initSplashScreen() {
        splashScreen = new JWindow(this);
        splashScreen.setAlwaysOnTop(true);
        splashScreen.setSize(600,400);
        splashScreen.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        JLabel gif = new JLabel(splashGif);
        panel.setLayout(new BorderLayout());
        panel.setBackground(mainOrange);
        splashScreen.setBackground(mainOrange);
        splashScreen.setForeground(mainOrange);
        panel.add(gif);
        splashScreen.add(panel);
        splashScreen.setVisible(true);
    }


    public static void main(String[] args) {
        JFrame window = new MediManagerGUI();
    }

    public JList getDisplayedList() {
        return displayedList;
    }

    public MedicineCabinet getMedCab() {
        return medCab;
    }

    public void setMedCab(MedicineCabinet medCab) {
        this.medCab = medCab;
    }

    public DefaultListModel<String> getMedsModel() {
        return medsModel;
    }

    public JPanel getDetailWindow() {
        return detailWindow;
    }

    public JToggleButton getDmButton() {
        return dmButton;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JPanel getListPanel() {
        return listPanel;
    }

    public JPanel getRightSidebar() {
        return rightSidebar;
    }

    public JPanel getRightButtonPanel() {
        return rightButtonPanel;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public JButton getTakePillButton() {
        return takePillButton;
    }

    public JButton getIncreasePillNumButton() {
        return increasePillNumButton;
    }

    public JButton getDecreasePillNumButton() {
        return decreasePillNumButton;
    }

    public JButton getIncreaseRefillNumButton() {
        return increaseRefillNumButton;
    }

    public JButton getDecreaseRefillNumButton() {
        return decreaseRefillNumButton;
    }

    public JLabel getTimeLeft() {
        return timeLeft;
    }

    public JLabel getNameText() {
        return nameText;
    }

    public JLabel getRefillNumText() {
        return refillNumText;
    }

    public JLabel getDoseText() {
        return doseText;
    }

    public JLabel getFoodText() {
        return foodText;
    }

    public JLabel getPillNumText() {
        return pillNumText;
    }

    public JPanel getDetailsButtonTopRow() {
        return detailsButtonTopRow;
    }

    public JPanel getDetailsButtonBotRow() {
        return detailsButtonBotRow;
    }
}
