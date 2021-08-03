package ca.ualberta.libmigproto.transformation;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

import java.util.List;

public class HardCodedTransformationRuleSet implements TransformationRuleSet {

    /*
    // Apache, target
        var builder = CSVFormat.DEFAULT.builder();
        builder = builder.setDelimiter(';');
        builder = builder.setQuote('"');
        builder = builder.setRecordSeparator('\n');
    }

    // super, source
        var builder = new CsvPreference.Builder('"', ';', "\n");
    }
     */

    @Override
    public TransformationRule getRule(Node source) {
        String block = "";
        if (source.toString().startsWith("var builder")) {
            block = """
                    {
                        var builder = CSVFormat.DEFAULT.builder();
                        builder = builder.setDelimiter(p0);
                        builder = builder.setQuote(p1);
                        builder = builder.setRecordSeparator(p2);
                    }
            """;
        } else if (source.toString().startsWith("var reader")) {
            block = """
                    {
                    var reader = new CSVParser(new StringReader(csv), preferences);
                    }
                    """;
        } else if (source.toString().startsWith("var records")) {
            block = """
                    {
                    var results = reader.getRecords();
                    var firstRecord = results.get(p3);
                    var records = firstRecord.stream().toList();
                    }
                    """;
        }

        var target = JavaParser.parseBlock(block).getStatements();

        return new TransformationRule(source, target);
    }
}
