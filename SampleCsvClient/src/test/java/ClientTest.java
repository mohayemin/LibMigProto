import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class ClientTest {

    @Test
    public void firstRow() throws IOException {
        var client = new CsvMigration();
        var csv = client.readFirstRow();
        Assertions.assertEquals("Taqee", csv.get(0));
        Assertions.assertEquals("Jan 1, 2020", csv.get(1));
    }
}
