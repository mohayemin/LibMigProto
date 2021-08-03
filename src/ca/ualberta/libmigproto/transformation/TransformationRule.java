package ca.ualberta.libmigproto.transformation;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

public class TransformationRule {
    public final Node sourceApi;
    public final NodeList<?> targetApi;

    public TransformationRule(Node source, NodeList<?> target) {
        this.sourceApi = source;
        this.targetApi = target;
    }

    public boolean apply(BlockStmt parentBlock) {
        var index = parentBlock.getChildNodes().indexOf(sourceApi);
        for (var targetApiPart: targetApi) {
            parentBlock.addStatement(++index, (Statement) targetApiPart);
        }

        sourceApi.remove();

        return false;
    }
}

