package persistance;

import model.Medication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonTest {
    protected void checkMedicine(Medication med, String name, int dosage, boolean takeWithFood, int numPillsRemaining,
                                 int numRefillsRemaining, boolean readyToTake, long nextTime, double frequency) {
        assertEquals(name, med.getName());
        assertEquals(dosage, med.getDosage());
        assertEquals(takeWithFood, med.takeWithFood());
        assertEquals(numPillsRemaining, med.getNumPillsRemaining());
        assertEquals(numRefillsRemaining, med.getNumRefillsRemaining());
        assertEquals(readyToTake, med.getMediTimer().isReadyToTake());
        assertTrue((med.getMediTimer().getNextTime().getTime() - nextTime < 1000000000L)
        && (med.getMediTimer().getNextTime().getTime() - nextTime > -1000000000L));
        assertEquals(frequency, med.getMediTimer().getFrequency());
    }
}
