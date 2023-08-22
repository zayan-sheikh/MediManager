package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;

import java.util.ArrayList;
import java.util.List;

// Class represents the list of all medicine the user is taking (that they have added)
public class MedicineCabinet implements Writable {
    private List<Medication> cabinet;

    // EFFECTS: constructs an empty MedicineCabinet list
    public MedicineCabinet() {
        this.cabinet = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds desired medicine to cabinet and returns true if not already in cabinet, otherwise false
    public boolean addMedToCab(Medication med) {
        if (this.cabinet.contains(med)) {
            return false;
        } else {
            cabinet.add(med);
            return true;
        }
    }

    // REQUIRES: med is already in cabinet
    // MODIFIES: this
    // EFFECTS: removes med from cabinet
    public void removeMedFromCab(Medication med) {
        this.cabinet.remove(med);
        EventLog.getInstance().logEvent(new Event("Medication '" + med.getName() + "' was removed."));
    }

    // EFFECTS: returns cabinet list, with the most recently added being last in the list
    public List<Medication> getCabinet() {
        return this.cabinet;
    }

    // EFFECTS: clears cabinet list
    public void clearCabinet() {
        this.cabinet.clear();
        EventLog.getInstance().logEvent(new Event("All medications were cleared."));
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("cabinet", cabinetToJson());
        return json;
    }

    // EFFECTS: returns medications in this cabinet as a JSON array
    public JSONArray cabinetToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Medication med : cabinet) {
            jsonArray.put(med.toJson());
        }
        return jsonArray;
    }


}
