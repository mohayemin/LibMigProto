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

    public boolean apply(CompilationUnit cu) {
        var sourceParent = (BlockStmt) sourceApi.getParentNode().get();
        var index = sourceParent.getChildNodes().indexOf(sourceApi);

        for (var targetApiPart: targetApi) {
            sourceParent.addStatement(++index, (Statement) targetApiPart);
        }

        sourceApi.remove();

        return false;
    }
}

