package app;

// 1. Novos imports da arquitetura do seu amigo
import core.lexer.Scanner;
import core.parser.ast.nodes.ProgramNode;
import core.parser.parser;
import core.semantic.SemanticAnalyzer;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestSemantic {

    public static void main(String[] args) {
        System.out.println("=====================================================");
        System.out.println("   RUNNING SEMANTIC TESTS (FILES)    ");
        System.out.println("=====================================================\n");

        Path testDir = Paths.get("src/test/schemeTests");

        if (!Files.exists(testDir)) {
            System.err.println("ERROR: test directory not found at: " + testDir.toAbsolutePath());
            return;
        }

        try (Stream<Path> paths = Files.list(testDir)) {
            List<Path> files =
                    paths.filter(Files::isRegularFile)
                            .filter(p -> p.toString().endsWith(".txt"))
                            .sorted()
                            .collect(Collectors.toList());

            for (Path filePath : files) {
                runTestFromFile(filePath.toString());
            }
        } catch (Exception e) {
            System.err.println("Error reading test directory: " + e.getMessage());
        }
    }

    private static void runTestFromFile(String filePath) {
        System.out.println(">>> Processing file: " + filePath);

        try {
            Path path = Paths.get(filePath);
            String codigoScheme = Files.readString(path);

            Scanner scanner = new Scanner(new StringReader(codigoScheme));
            parser p = new parser(scanner);
            ProgramNode raiz = (ProgramNode) p.parse().value;

            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
            raiz.accept(semanticAnalyzer);

            if (semanticAnalyzer.hasErrors()) {
                System.err.println("Semantic errors found:");
                for (String erro : semanticAnalyzer.getErrors()) {
                    System.err.println("   - " + erro);
                }
                System.err.println();
            } else {
                System.out.println("Success! No semantic errors found.\n");
            }

        } catch (Exception e) {
            System.err.println("Sintatic error: " + e.getMessage() + "\n");
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
    }
}
