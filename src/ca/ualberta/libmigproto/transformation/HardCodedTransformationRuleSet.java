package ca.ualberta.libmigproto.transformation;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
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
        var block = JavaParser.parseBlock("""
                {
                    var builder = CSVFormat.DEFAULT.builder();
                    builder = builder.setDelimiter(p1);
                    builder = builder.setQuote(p2);
                    builder = builder.setRecordSeparator(p3);
                }
                                        """);
        var target = block.getStatements();

        return new TransformationRule(source, target);
    }
}
