package persistance;


import model.Medication;
import model.MedicineCabinet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from the given source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public MedicineCabinet read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCabinet(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses cabinet from json file and returns it
    private MedicineCabinet parseCabinet(JSONObject json) {
        MedicineCabinet cabinet = new MedicineCabinet();
        addMeds(cabinet, json);
        return cabinet;
    }

    // MODIFIES: cabinet
    // EFFECTS: adds required medicine to cabinet from json
    private void addMeds(MedicineCabinet cabinet, JSONObject json) {
        JSONArray jsonArray = json.getJSONArray("cabinet");
        for (Object jsonObj : jsonArray) {
            JSONObject nextMed = (JSONObject) jsonObj;
            addMed(cabinet, nextMed);
        }
    }

    // MODIFIES: cabinet
    // EFFECTS: adds singular Medication to cabinet using json file
    private void addMed(MedicineCabinet cabinet, JSONObject nextMed) {
        String name = nextMed.getString("name");
        int dosage = nextMed.getInt("dosage");
        boolean takeWithFood = nextMed.getBoolean("takeWithFood");
        int numPillsRemaining = nextMed.getInt("numPillsRemaining");
        int numRefillsRemaining = nextMed.getInt("numRefillsRemaining");
        JSONObject mediTimer = (JSONObject) nextMed.get("mediTimer");
        long nextTime = mediTimer.getLong("nextTime");
        double frequency = mediTimer.getDouble("frequency");
        boolean readyToTake = mediTimer.getBoolean("readyToTake");

        Medication med = new Medication(name, dosage, frequency, takeWithFood, numPillsRemaining,
                numRefillsRemaining);
        med.getMediTimer().setReadyToTake(false);
        med.getMediTimer().getNextTime().setTime(nextTime);

        cabinet.addMedToCab(med);
    }


}
