package ca.ualberta.libmigproto.usageminer;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsageMiner {
    private final SourceRoot clientSourceRoot;
    private final JavaSymbolSolver symbolResolver;
    private final CombinedTypeSolver typeResolver;
    private final JavaParserTypeSolver libTypeResolver;

    public UsageMiner(Path clientSourcePath, Path libPath) {
        this.libTypeResolver = new JavaParserTypeSolver(libPath.toFile());
        this.typeResolver = new CombinedTypeSolver(
                libTypeResolver,
                new JavaParserTypeSolver(clientSourcePath.toFile()),
                new ReflectionTypeSolver()
        );
        this.symbolResolver = new JavaSymbolSolver(typeResolver);

        this.clientSourceRoot = new SourceRoot(clientSourcePath);
        this.clientSourceRoot.getParserConfiguration().setSymbolResolver(this.symbolResolver);
    }

    public void mine() throws IOException {
        var clientCompilationUnits = clientSourceRoot.tryToParse().stream()
                .filter(ParseResult::isSuccessful)
                .map(ParseResult::getResult)
                .map(Optional::get)
                .collect(Collectors.toList());

        clientCompilationUnits.forEach(this::mineCompilationUnit);
    }

    private void mineCompilationUnit(CompilationUnit compilationUnit) {
        var methodCalls = compilationUnit.findAll(MethodCallExpr.class);

        methodCalls.forEach(call -> {
            try {
                var declaration = call.resolveInvokedMethod();
                if (declaration.getPackageName().startsWith("org.supercsv")) {
                    var usage = new LibraryUsage(
                            compilationUnit,
                            call,
                            declaration
                    );
                    System.out.println(usage.toCSV());
                }
            } catch (Exception e) {
                // Not an API method invocation.
                // TODO: Possibly not the best way to do this.
            }
        });
    }
}
