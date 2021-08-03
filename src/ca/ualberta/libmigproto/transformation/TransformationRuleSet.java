package ca.ualberta.libmigproto.transformation;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;

public interface TransformationRuleSet {
    TransformationRule getRule(Node source);
}

