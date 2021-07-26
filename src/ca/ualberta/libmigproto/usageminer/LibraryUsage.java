package ca.ualberta.libmigproto.usageminer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;

public class LibraryUsage {
    public final String clientFilepath;
    public final int line;
    public final int col;
    public final String invocation;
    public final String declaration;

    public LibraryUsage(String clientFilepath,
                        int line,
                        int col,
                        String invocation,
                        String declaration) {
        this.clientFilepath = clientFilepath;
        this.line = line;
        this.col = col;
        this.invocation = invocation;
        this.declaration = declaration;
    }

    public LibraryUsage(CompilationUnit compilationUnit, MethodCallExpr call, ResolvedMethodDeclaration resolvedCall) {
        this(compilationUnit.getStorage().get().getPath().toString()
                , call.getRange().get().begin.line
                , call.getRange().get().begin.column,
                call.toString(), resolvedCall.getQualifiedSignature()
        );
    }

    public String toCSV() {
        return String.format("\"%s(%d:%d)\" , \"%s\" , \"%s\"",clientFilepath, line, col, invocation, declaration);
    }
}
