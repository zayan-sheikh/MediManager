package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

// test class for MediTimeTest
public class MediTimeTest {
    MediTime testTimer;

    @BeforeEach
    public void runBefore() {
        testTimer = new MediTime(4.5);
    }

    @Test
    public void testConstructor(){
        assertTrue(testTimer.isReadyToTake());
        assertEquals(4.5, testTimer.getFrequency());
    }

    @Test
    public void testSetFields(){
        testTimer.setFrequency(5.6);
        assertEquals(5.6,testTimer.getFrequency());

        testTimer.setFrequency(2.9);
        assertEquals(2.9,testTimer.getFrequency());

        testTimer.setReadyToTake(false);
        assertFalse(testTimer.isReadyToTake());

        testTimer.setReadyToTake(true);
        assertTrue(testTimer.isReadyToTake());
    }

    @Test
    public void testReset(){
        Date date = new Date();
        testTimer.setNextTime(date);
        assertEquals(testTimer.getNextTime(), date);
        testTimer.resetTimer();
        Date curr = new Date();
        assertEquals(testTimer.getNextTime().getTime(), (curr.getTime() + testTimer.getFrequency() * 3600 * 1000));
        assertFalse(testTimer.isReadyToTake());
        }

    @Test
    public void testPlural() {
        String s = testTimer.pluralize(5);
        assertEquals(s, "s");

        String noS = testTimer.pluralize(1);
        assertEquals(noS, "");
    }

    @Test
    public void testDifference() {
        String diff = testTimer.getDifferenceTime();
        assertEquals(diff, "0 seconds remain until your next dose.");

        testTimer.resetTimer();
        diff = testTimer.getDifferenceTime();
        assertEquals(diff, "4 hours, 30 minutes and 0 seconds remain until your next dose.");
    }

    @Test
    public void testJsonMed() {
        Medication testMed = new Medication("test", 2, 12.5, true, 5,
                1);

        JSONObject mediTimer = testMed.getMediTimer().toJson();

        assertEquals(testMed.getMediTimer().getFrequency(), mediTimer.getDouble("frequency"));
        assertEquals(testMed.getMediTimer().isReadyToTake(), mediTimer.getBoolean("readyToTake"));
        assertEquals(testMed.getMediTimer().getNextTime().getTime(), mediTimer.getLong("nextTime"));
    }
}
