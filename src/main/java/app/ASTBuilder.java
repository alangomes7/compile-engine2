package app;

import java.io.StringReader;

import core.ast.nodes.ProgramNode;
import core.lexer.Scanner;
import scanner.parser;
import scanner.sym;

/**
 * Builds an Abstract Syntax Tree (AST) from a Scheme source code string. The resulting AST can be
 * retrieved and passed to a code generator.
 */
public class ASTBuilder {

    private ProgramNode ast;

    /**
     * Constructs an ASTBuilder and immediately parses the given code.
     *
     * @param code the Scheme source code as a string
     * @throws Exception if lexical or syntactic analysis fails
     */
    public ASTBuilder(String code) throws Exception {
        parse(code);
    }

    /**
     * Parses the provided code and stores the resulting AST.
     *
     * @param code the Scheme source code string
     * @throws Exception if the scanner or parser encounters an error
     */
    public void parse(String code) throws Exception {
        
        System.out.println("=== Debugging Scanner ===");
        Scanner scanner = new Scanner(new StringReader(code));
        this.debugScanner(code);
        System.out.println("=== End Scanner Debug ===");

        parser p = new parser(scanner);
        this.ast = (ProgramNode) p.parse().value;
    }

    /**
     * Returns the built AST.
     *
     * @return the root ProgramNode of the AST, or null if parse() hasn't been called successfully yet
     */
    public ProgramNode getAST() {
        return ast;
    }

    public void debugScanner(String code) throws Exception {
        Scanner scanner = new Scanner(new java.io.StringReader(code));
        java_cup.runtime.Symbol token;
        while ((token = scanner.next_token()).sym != sym.EOF) {
            System.out.println("Token: " + token.sym + " value: " + token.value);
        }
    }
}