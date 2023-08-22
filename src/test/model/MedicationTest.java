package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Test class for Medication class
class MedicationTest {
    private Medication testMed;

    @BeforeEach
    public void runBefore(){
        testMed = new Medication("test", 2, 12.5, true, 5,
                1);
    }

    @Test
    public void testConstructor() {
        assertEquals("test",testMed.getName());
        assertEquals(2,testMed.getDosage());
        assertEquals(12.5,testMed.getMediTimer().getFrequency());
        assertTrue(testMed.takeWithFood());
        assertEquals(5,testMed.getNumPillsRemaining());
        assertEquals(1,testMed.getNumRefillsRemaining());
        assertTrue(testMed.getMediTimer().isReadyToTake());
    }

    @Test
    public void testSetPillNum() {
        testMed.setNumPillsRemaining(7);
        assertEquals(testMed.getNumPillsRemaining(), 7);

        testMed.setNumPillsRemaining(9);
        assertEquals(testMed.getNumPillsRemaining(), 9);
    }

    @Test
    public void testAddAndRemoveOnePill() {
        testMed.addOnePill();
        assertEquals(testMed.getNumPillsRemaining(), 6);

        testMed.addOnePill();
        assertEquals(testMed.getNumPillsRemaining(), 7);

        assertTrue(testMed.removeOnePill());
        assertEquals(testMed.getNumPillsRemaining(), 6);

        assertTrue(testMed.removeOnePill());
        assertEquals(testMed.getNumPillsRemaining(), 5);

        testMed.setNumPillsRemaining(0);
        assertFalse(testMed.removeOnePill());

        testMed.addOnePill();
        assertEquals(testMed.getNumPillsRemaining(), 1);

        assertTrue(testMed.removeOnePill());
        assertEquals(testMed.getNumPillsRemaining(), 0);
    }

    @Test
    public void testEatPill(){
        assertTrue(testMed.eatPill());
        assertFalse(testMed.getMediTimer().isReadyToTake());
        assertEquals(3, testMed.getNumPillsRemaining());

        assertTrue(testMed.eatPill());
        assertEquals(1, testMed.getNumPillsRemaining());

        assertFalse(testMed.eatPill());

        testMed.addOnePill();
        assertTrue(testMed.eatPill());
        assertEquals(0, testMed.getNumPillsRemaining());
    }

    @Test
    public void testAddAndRemoveOneRefill() {
        testMed.addOneRefill();
        assertEquals(testMed.getNumRefillsRemaining(), 2);

        testMed.addOneRefill();
        assertEquals(testMed.getNumRefillsRemaining(), 3);

        assertTrue(testMed.removeOneRefill());
        assertEquals(testMed.getNumRefillsRemaining(), 2);

        assertTrue(testMed.removeOneRefill());
        assertEquals(testMed.getNumRefillsRemaining(), 1);

        testMed.setNumRefillsRemaining(0);
        assertFalse(testMed.removeOneRefill());

        testMed.addOneRefill();
        assertEquals(testMed.getNumRefillsRemaining(), 1);

        assertTrue(testMed.removeOneRefill());
        assertEquals(testMed.getNumRefillsRemaining(), 0);
    }

    @Test
    public void testJsonMed() {
        JSONObject json = testMed.toJson();
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