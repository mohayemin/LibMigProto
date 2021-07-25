package ca.ualberta.libmigproto.usageminer;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MethodMiner {
    public ArrayList<MethodDeclaration> findAllMethods(Path sourceRootPath) throws IOException {
        var sourceRoot = new SourceRoot(sourceRootPath);
        var compilationUnits = sourceRoot.tryToParse().stream()
                .filter(ParseResult::isSuccessful)
                .map(ParseResult::getResult)
                .map(Optional::get)
                .collect(Collectors.toList());

        var visitor = new MethodCollectorVisitor();
        var methods = new ArrayList<MethodDeclaration>();
        compilationUnits.forEach(cu -> visitor.visit(cu, methods));

        return methods;
    }
}

class MethodCollectorVisitor extends VoidVisitorAdapter<List<MethodDeclaration>> {
    @Override
    public void visit(MethodDeclaration md, List<MethodDeclaration> collection) {
        super.visit(md, collection);
        collection.add(md);
    }
}