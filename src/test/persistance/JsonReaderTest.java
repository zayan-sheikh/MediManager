package persistance;

import model.MedicineCabinet;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderFileNotFound() {
        JsonReader reader = new JsonReader("./data/fakeFile.json");
        try {
            MedicineCabinet cabinet = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyCabinet() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyCabinet.json");
        try {
            MedicineCabinet cabinet = reader.read();
            assertEquals(cabinet.getCabinet().size(), 0);
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testGenericCabinet.json");
        try {
            MedicineCabinet cabinet = reader.read();

            checkMedicine(cabinet.getCabinet().get(0), "Metformin", 2, true, 53, 2,
                    false, 1690446360217L, 48);

            checkMedicine(cabinet.getCabinet().get(1), "Amoxicillin", 1, false, 64, 2,
                    false, 1690302365524L, 8);

        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
}
