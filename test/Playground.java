import ca.ualberta.libmigproto.transformation.HardCodedTransformationRuleSet;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class Playground {
    String code = """
var builder = CSVFormat.DEFAULT.builder();
builder = builder.setDelimiter(p1);
builder = builder.setQuote(p2);
builder = builder.setRecordSeparator(p3);
""";

    @Test
    public void parse() {
        var block = JavaParser.parseBlock("{" + code + "}");
        printBlock(block);
    }

    public void printBlock(BlockStmt blk) {
        for (var child : blk.getChildNodes()) {
            printNode(child, 0);
        }
    }

    public void printNode(Node node, int level) {
        System.out.println(" ".repeat(level * 2) + node.toString() + "  ::" + node.getClass().getSimpleName());
        for (var child : node.getChildNodes()) {
            printNode(child, level + 1);
        }
    }

    @Test
    public void applyTransformation() throws IOException {
        var ruleSet = new HardCodedTransformationRuleSet();
        var cu = JavaParser.parse(Path.of("D:\\PhD\\LibMigProto\\SampleCsvClient\\src\\main\\java\\SuperCsvClient.java.original"));
        var sourceApi = cu.findFirst(ExpressionStmt.class).get();

        System.out.println("=== before ===");
        System.out.println(cu);

        System.out.println(sourceApi);

        var rule =  ruleSet.getRule(sourceApi);
        var success = rule.apply(cu);

        System.out.println("=== after ===");
        System.out.println(cu);

        Files.write(Path.of("D:\\PhD\\LibMigProto\\SampleCsvClient\\src\\main\\java\\CsvMigration.java"), Collections.singleton(cu.toString()), StandardCharsets.UTF_8);
    }
}
