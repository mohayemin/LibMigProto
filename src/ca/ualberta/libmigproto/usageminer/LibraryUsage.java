package ca.ualberta.libmigproto.usageminer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;

public record LibraryUsage(CompilationUnit compilationUnit,
                           Node caller,
                           MethodCallExpr call,
                           ResolvedMethodDeclaration resolvedCall) {

    public String toCSV() {
        var storage = compilationUnit.getStorage().get();
        var sourceRoot = storage.getSourceRoot();
        var clientFilepath = sourceRoot.relativize(storage.getPath());
        var start = call.getRange().get().begin;
        var invocation = call.toString();
        var declaration = resolvedCall.getQualifiedSignature();
        return String.format("\"%s(%d:%d)\" , \"%s\" , \"%s\"", clientFilepath, start.line, start.column, invocation, declaration);
    }
}
