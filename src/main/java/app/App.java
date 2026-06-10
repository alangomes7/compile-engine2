package app;

import core.codegen.PythonGeneratorVisitor;
import core.parser.ast.ASTPrinter;
import core.parser.ast.nodes.ProgramNode;
import core.semantic.SemanticAnalyzer;
import core.utils.FileOperations;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {

        int passed = 0;
        int failed = 0;

        List<Integer> passedTests = new ArrayList<>();
        List<Integer> failedTests = new ArrayList<>();

        for (int fileTest = 1; fileTest <= 23; fileTest++) {

            String filePath = "src/test/schemeTests/test" + fileTest + ".txt";
            System.out.println("\n==================================================");
            System.out.println("Executing Test File: test" + fileTest + ".txt");
            System.out.println("Path: " + filePath);
            System.out.println("==================================================\n");

            try {

                String code = FileOperations.readSchemeCodeFromFile(filePath);
                System.out.println("--- ---- Input phase --- ----\n");
                System.out.println(code + "\n");

                // Scanner + Parser (AST)
                ASTBuilder builder = new ASTBuilder(code);
                ProgramNode ast = builder.getAST();
                System.out.println("AST built successfully:\n");
                ASTPrinter printer = new ASTPrinter();
                ast.accept(printer);

                // --- ---- SEMANTIC PHASE --- ----
                System.out.println("\n--- ---- Semantic phase --- ----\n");
                SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
                ast.accept(semanticAnalyzer);

                if (semanticAnalyzer.hasErrors()) {
                    System.out.println("Semantic Errors Found:");
                    for (String err : semanticAnalyzer.getErrors()) {
                        System.out.println("   - " + err);
                    }
                    // Lança exceção para interromper o fluxo e cair no bloco catch (marcando como
                    // FAILED)
                    throw new RuntimeException("Semantic phase validation failed.");
                } else {
                    System.out.println("Semantic analysis passed! No errors found.");
                }
                // --------------------------------

                // Python code generator
                System.out.println("\nGenerating Python Code...\n");
                PythonGeneratorVisitor codeGenerator = new PythonGeneratorVisitor();
                String pythonCode = codeGenerator.visit(ast);
                System.out.println(pythonCode);

                passed++;
                passedTests.add(fileTest);

                System.out.println("\n✅ TEST " + fileTest + " PASSED\n");

            } catch (Exception ex) {

                failed++;
                failedTests.add(fileTest);
                System.out.println("\n❌ TEST " + fileTest + " FAILED\n");
                System.getLogger(App.class.getName())
                        .log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }

        System.out.println("\n==================================================");
        System.out.println("FINAL TEST SUMMARY");
        System.out.println("==================================================");
        System.out.println("✅ Passed: " + passed);
        System.out.println("❌ Failed: " + failed);
        System.out.println("\nPassed Tests: " + passedTests);
        System.out.println("Failed Tests: " + failedTests);
        System.out.println("\n==================================================");
    }
}
