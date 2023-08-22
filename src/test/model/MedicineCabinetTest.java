package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Test class for MedicineCabinet Class
public class MedicineCabinetTest {
    MedicineCabinet testCab;
    Medication testMed;
    Medication testMed2;
    @BeforeEach
    public void runBefore() {
        testCab = new MedicineCabinet();
        testMed = new Medication("test", 2, 12.5, true, 5,
                1);
        testMed2 = new Medication("test2", 3, 1, false, 56,
                5);
    }

    @Test
    public void testConstructor() {
        assertEquals(0,testCab.getCabinet().size());
    }

    @Test
    public void testAddOnePill() {
        testCab.addMedToCab(testMed);
        assertEquals(1,testCab.getCabinet().size());
        assertEquals(testMed,testCab.getCabinet().get(0));
    }

    @Test
    public void testAddMultiPill() {
        insertTestPills();
    }

    private void insertTestPills() {
        assertTrue(testCab.addMedToCab(testMed));
        assertEquals(1,testCab.getCabinet().size());
        assertEquals(testMed,testCab.getCabinet().get(0));

        assertFalse(testCab.addMedToCab(testMed));
        assertEquals(1,testCab.getCabinet().size());

        assertTrue(testCab.addMedToCab(testMed2));
        assertEquals(2,testCab.getCabinet().size());
        assertEquals(testMed2,testCab.getCabinet().get(1));
    }

    @Test
    public void testClearMeds() {
        insertTestPills();
        testCab.clearCabinet();
        assertEquals(0,testCab.getCabinet().size());

        testCab.clearCabinet();
        assertEquals(0,testCab.getCabinet().size());
    }

    @Test
    public void testRemoveMeds() {
        insertTestPills();
        testCab.removeMedFromCab(testMed2);
        assertEquals(1,testCab.getCabinet().size());
        assertTrue(testCab.getCabinet().contains(testMed));
        assertFalse(testCab.getCabinet().contains(testMed2));

        testCab.removeMedFromCab(testMed);
        assertEquals(0,testCab.getCabinet().size());
        assertFalse(testCab.getCabinet().contains(testMed));
    }

    @Test
    public void testJsonMed() {
        testCab.addMedToCab(testMed);
        JSONObject jsonCab = testCab.toJson();
        JSONArray cabinet = jsonCab.getJSONArray("cabinet");
        JSONObject json = (JSONObject) cabinet.get(0);
        assertEquals("test",json.get("name"));
        assertEquals(testMed.getDosage(), json.get("dosage"));
        assertEquals(testMed.getNumPillsRemaining(), json.get("numPillsRemaining"));
        assertEquals(testMed.getNumRefillsRemaining(), json.get("numRefillsRemaining"));
        JSONObject mediTimer = (JSONObject) json.get("mediTimer");
        assertEquals(testMed.getMediTimer().getFrequency(), mediTimer.getDouble("frequency"));
        assertEquals(testMed.getMediTimer().isReadyToTake(), mediTimer.getBoolean("readyToTake"));
        assertEquals(testMed.getMediTimer().getNextTime().getTime(), mediTimer.getLong("nextTime"));
    }

}
