package persistance;

import model.Medication;
import model.MedicineCabinet;
import org.junit.jupiter.api.Test;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            MedicineCabinet cabinet = new MedicineCabinet();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyCabinet() {
        try {
            MedicineCabinet cabinet = new MedicineCabinet();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyCabinet.json");
            writer.open();
            writer.write(cabinet);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyCabinet.json");
            cabinet = reader.read();
            assertEquals(0, cabinet.getCabinet().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            MedicineCabinet cabinet = new MedicineCabinet();
            Medication med1 = new Medication("Metformin", 2, 48, true,
                    53, 2);
            Medication med2 = new Medication("Amoxicillin", 1, 8, false,
                    64, 2);
            med1.getMediTimer().setReadyToTake(false);
            med2.getMediTimer().setReadyToTake(false);
            med2.getMediTimer().getNextTime().setTime(1690446360217L);
            med2.getMediTimer().getNextTime().setTime(1690302365524L);

            cabinet.addMedToCab(med1);
            cabinet.addMedToCab(med2);

            JsonWriter writer = new JsonWriter("./data/testWriterGenericCabinet.json");
            writer.open();
            writer.write(cabinet);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGenericCabinet.json");
            checkMedicine(cabinet.getCabinet().get(0), "Metformin", 2, true, 53, 2,
                    false, 1690446360217L, 48);

            checkMedicine(cabinet.getCabinet().get(1), "Amoxicillin", 1, false, 64, 2,
                    false, 1690302365524L, 8);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
