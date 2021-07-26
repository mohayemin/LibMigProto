package ca.ualberta.libmigproto.usageminer;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsageMiner {
    private final SourceRoot clientSourceRoot;
    private final JavaSymbolSolver symbolResolver;
    private final CombinedTypeSolver typeResolver;
    private final JavaParserTypeSolver libTypeResolver;
    private final String libPackage;
    private List<LibraryUsage> usages = new ArrayList<>();

    public UsageMiner(Path clientSourcePath, Path libPath, String libPackage) {
        this.libTypeResolver = new JavaParserTypeSolver(libPath.toFile());
        this.typeResolver = new CombinedTypeSolver(
                libTypeResolver,
                new JavaParserTypeSolver(clientSourcePath.toFile()),
                new ReflectionTypeSolver()
        );
        this.libPackage = libPackage;
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
        compilationUnit.findAll(MethodCallExpr.class).forEach(call -> processMethodCall(compilationUnit, call));
        compilationUnit.findAll(ObjectCreationExpr.class).forEach(objCreate -> processObjectCreate(compilationUnit, objCreate));
    }

    private void processObjectCreate(CompilationUnit compilationUnit, ObjectCreationExpr objectCreate) {
        var declaration = objectCreate.resolveInvokedConstructor();
        if (declaration.getPackageName().startsWith(libPackage)) {

        }
    }

    private void processMethodCall(CompilationUnit compilationUnit, MethodCallExpr call) {
        try {
            var declaration = call.resolveInvokedMethod();
            if (declaration.getPackageName().startsWith(libPackage)) {
                var usage = new LibraryUsage(
                        compilationUnit,
                        call,
                        call.getParentNode().get(),
                        declaration
                );
                usages.add(usage);
                System.out.println(usage.toCSV());
            }
        } catch (Exception e) {
            // Not an API method invocation.
            // TODO: Possibly not the best way to do this.
        }
    }
}
