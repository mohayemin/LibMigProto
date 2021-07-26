package ca.ualberta.libmigproto.usageminer;


import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        var libPath = Path.of("D:\\PhD\\LibMigProto-Data\\supercsv\\lib\\super-csv\\super-csv\\src\\main\\java");
        var clientPath = Path.of("D:\\PhD\\LibMigProto-Data\\supercsv\\clients\\algem\\src");
        var usageMiner = new UsageMiner(clientPath, libPath, "org.supercsv");
        usageMiner.mine();
    }
}
