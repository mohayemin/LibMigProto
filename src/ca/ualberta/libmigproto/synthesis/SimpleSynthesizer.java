package ca.ualberta.libmigproto.synthesis;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.LiteralExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;

import java.util.Comparator;
import java.util.List;

public class SimpleSynthesizer {
    public void fillHole(Node sketch, List<LiteralExpr> fillers) {
        var holes = sketch.findAll(NameExpr.class)
                .stream()
                .filter(name -> name.getNameAsString().startsWith("p_"))
                .sorted(Comparator.comparing(NodeWithSimpleName::getNameAsString))
                .toList();

        for (int i = 0; i < 4; i++) {
            holes.get(i).replace(fillers.get(i));
        }
    }
}
