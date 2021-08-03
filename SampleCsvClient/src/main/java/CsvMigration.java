import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;
import org.apache.commons.csv.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class SuperCsvClient {

    String csv = "Taqee;\"Jan 1, 2020\"";

    public List<String> readFirstRow() throws IOException {
        var builder = CSVFormat.DEFAULT.builder();
        builder = builder.setDelimiter(p0);
        builder = builder.setQuote(p1);
        builder = builder.setRecordSeparator(p2);
        var preferences = builder.build();
        var reader = new CSVParser(new StringReader(csv), preferences);
        var results = reader.getRecords();
        var firstRecord = results.get(p3);
        var records = firstRecord.stream().toList();
        return records;
    }
}

