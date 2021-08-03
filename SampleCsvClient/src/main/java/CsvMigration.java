import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;
import org.apache.commons.csv.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class CsvMigration {

    String csv = "Taqee;\"Jan 1, 2020\"";

    public List<String> readFirstRow() throws IOException {
        var builder = CSVFormat.DEFAULT.builder();
        builder = builder.setQuote('"');
        builder = builder.setDelimiter(';');
        builder = builder.setRecordSeparator('"');
        var preferences = builder.build();
        var reader = new CSVParser(new StringReader(csv), preferences);
        var results = reader.getRecords();
        var firstRecord = results.get(0);
        var records = firstRecord.stream().toList();
        return records;
    }
}

