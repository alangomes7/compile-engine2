package app;

import core.ast.ASTPrinter;
import core.ast.nodes.ProgramNode;
import core.codegen.PythonGeneratorVisitor;
import utils.FileOperations;

public class App {
    public static void main(String[] args) {
        // [1-8]
        int fileTest = 8;
        String filePath = "src/main/resources/schemeTests/test" + fileTest + ".txt";

        try {
            String code = FileOperations.readSchemeCodeFromFile(filePath);
            System.out.println("Code read from file:\n" + code + "\n");
            
            // Scanner debug + AST
            System.out.println("=============================================\n");
            ASTBuilder builder = new ASTBuilder(code);
            ProgramNode ast = builder.getAST();
            System.out.println("AST built successfully.");
            ASTPrinter printer = new ASTPrinter();
            ast.accept(printer);

            // Python code generator
            System.out.println("=============================================\n");
            PythonGeneratorVisitor codeGenerator = new PythonGeneratorVisitor();
            String pythonCode = codeGenerator.visit(ast);
            System.out.println("\nGenerated Python Code:");
            System.out.println(pythonCode);

        } catch (Exception ex) {
            System.getLogger(App.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
