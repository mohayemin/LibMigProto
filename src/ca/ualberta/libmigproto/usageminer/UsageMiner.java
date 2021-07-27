package ca.ualberta.libmigproto.usageminer;

import ca.ualberta.libmigproto.config.ClientConfig;
import ca.ualberta.libmigproto.config.LibConfig;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsageMiner {
    private final SourceRoot clientSourceRoot;
    private final JavaSymbolSolver symbolResolver;
    private final CombinedTypeSolver typeResolver;
    private final JavaParserTypeSolver libTypeResolver;
    private final ClientConfig clientConfig;
    private final LibConfig libConfig;
    private List<LibraryUsage> usages = new ArrayList<>();

    public UsageMiner(ClientConfig clientConfig) throws GitAPIException {
        this.clientConfig = clientConfig;
        this.libConfig = clientConfig.libConfig;

        this.clientConfig.cloneRepository();
        this.libConfig.cloneRepository();

        this.libTypeResolver = new JavaParserTypeSolver(libConfig.getSourceRootPath().toFile());
        this.typeResolver = new CombinedTypeSolver(
                libTypeResolver,
                new JavaParserTypeSolver(clientConfig.getSourceRootPath().toFile()),
                new ReflectionTypeSolver()
        );
        this.symbolResolver = new JavaSymbolSolver(typeResolver);
        this.clientSourceRoot = new SourceRoot(clientConfig.getSourceRootPath());
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
        var types = compilationUnit.getTypes();
        types.forEach(this::mineType);
    }

    private void mineType(TypeDeclaration<?> typeDeclaration) {
        var methods = typeDeclaration.getMethods();
        methods.forEach(md -> mineMethod(md));
        // TODO: mine constructors
        // TODO: mine inline initializations
    }

    private void mineMethod(MethodDeclaration methodDeclaration) {
        var methodCalls = methodDeclaration.findAll(MethodCallExpr.class);
        methodCalls.forEach(mc -> processMethodCall(methodDeclaration, mc));
        // TODO: does it properly find calls in lambda?
    }


    private void processMethodCall(Node caller, MethodCallExpr call) {
        var compilationUnit = caller.findParent(CompilationUnit.class).get();
        ResolvedMethodDeclaration declaration;
        try {
            declaration = call.resolveInvokedMethod();
        } catch (Exception e) {
            // Not an API method invocation.
            // TODO: Possibly not the best way to do this.
            return;
        }
        if (declaration.getPackageName().startsWith(libConfig.rootPackage)) {
            var usage = new LibraryUsage(
                    compilationUnit,
                    caller,
                    call,
                    declaration
            );
            usages.add(usage);
            System.out.println(usage.toCSV());
        }

    }
}
