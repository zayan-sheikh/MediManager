package model;

import org.json.JSONObject;
import persistance.Writable;

// Class represents a medication that the user is taking
public class Medication implements Writable {
    private String name;
    private int dosage;
    private boolean takeWithFood;
    private int numPillsRemaining;
    private int numRefillsRemaining;
    private MediTime mediTimer;


    // REQUIRES: dosage > 0,
    // EFFECTS: constructs a medication with inputs for name, dosage (in # of pills per consumption), how often they are
    // to be taken (frequency; in hours after last consumption), if they are be to be taken with food, # of pills
    // remaining and # of refills remaining. Also creates a pill timer and initializes readyToTake to true.
    public Medication(String name, int dosage, double frequency, boolean takeWithFood, int numPillsRemaining,
                      int numRefillsRemaining) {
        this.name = name;
        this.dosage = dosage;
        this.takeWithFood = takeWithFood;
        this.numPillsRemaining = numPillsRemaining;
        this.numRefillsRemaining = numRefillsRemaining;
        this.mediTimer = new MediTime(frequency);
        EventLog.getInstance().logEvent(new Event("Medication '" + name + "' was created."));
    }

    // EFFECTS: returns name of medication
    public String getName() {
        return this.name;
    }

    // EFFECTS: returns dosage of medicine (in # of pills per consumption)
    public int getDosage() {
        return this.dosage;
    }

    // EFFECTS: returns the tracker of how often a medication should be taken & whether it is due to be taken at the
    // moment
    public MediTime getMediTimer() {
        return this.mediTimer;
    }

    // EFFECTS: returns how many pills of medicine remain
    public int getNumPillsRemaining() {
        return this.numPillsRemaining;
    }

    // EFFECTS: returns how many refills of medicine remain
    public int getNumRefillsRemaining() {
        return this.numRefillsRemaining;
    }

    public boolean takeWithFood() {
        return this.takeWithFood;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: sets the number of refills of medicine remaining to amount
    public void setNumRefillsRemaining(int amount) {
        this.numRefillsRemaining = amount;
    }

    // REQUIRES: amount >=0
    // MODIFIES: this
    // EFFECTS: sets the number of refills of pills remaining to amount
    public void setNumPillsRemaining(int amount) {
        this.numPillsRemaining = amount;
    }

    // MODIFIES: this
    // EFFECTS: adds one pill to remaining pills
    public void addOnePill() {
        ++numPillsRemaining;
    }

    // MODIFIES: this
    // EFFECTS: removes one pill from remaining pills, then returns true. If there are no pills left, returns false and
    // keeps the number of remaining pills at 0.
    public boolean removeOnePill() {
        if (getNumPillsRemaining() > 0) {
            --numPillsRemaining;
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds one refill to remaining refills
    public void addOneRefill() {
        ++numRefillsRemaining;
    }

    // MODIFIES: this
    // EFFECTS: removes one refill from remaining refills, then returns true. If there are no refills left, returns
    // false and keeps the number of remaining refills at 0.
    public boolean removeOneRefill() {
        if (getNumRefillsRemaining() > 0) {
            --numRefillsRemaining;
            return true;
        } else {
            return false;
        }
    }

    // REQUIRES: numPillsRemaining > 0
    // MODIFIES: this
    // EFFECTS: removes one dosage of pill from inventory, and restarts counter for timeTilNextPill, and returns true.
    // If not enough pills remain, then returns false without restarting counter or removing dosage from inventory.
    public boolean eatPill() {
        if (getNumPillsRemaining() >= getDosage()) {
            setNumPillsRemaining(getNumPillsRemaining() - getDosage());
            this.getMediTimer().resetTimer();
            EventLog.getInstance().logEvent(new Event("Medication '" + name + "' was taken."));
            return true;
        }  else {
            return false;
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("dosage", dosage);
        json.put("takeWithFood", takeWithFood);
        json.put("numPillsRemaining", numPillsRemaining);
        json.put("numRefillsRemaining", numRefillsRemaining);
        json.put("mediTimer", mediTimer.toJson());

        return json;
    }
}
