package app;

import static core.parser.sym.EOF;
import static core.parser.sym.LPAREN;
import static core.parser.sym.terminalNames;

import core.lexer.Scanner;
import core.lexer.models.atomic.LexerError; // IMPORT NOVO
import core.parser.ast.nodes.ProgramNode;
import core.parser.parser;
import java.io.StringReader;
import java.util.List;

public class ASTBuilder {

    private ProgramNode ast;

    public ASTBuilder(String code) throws Exception {
        parseInternal(code);
    }

    public final void parse(String code) throws Exception {
        parseInternal(code);
    }

    private void parseInternal(String code) throws Exception {
        Scanner scannerToDebug = new Scanner(new StringReader(code));
        Scanner scannerToParser = new Scanner(new StringReader(code));
        debugScanner(scannerToDebug);
        System.out.println("\n--- ---- Parser phase --- ----");
        System.out.println("Parser's expected ID for LPAREN: " + LPAREN);

        @SuppressWarnings("deprecation")
        parser p = new parser(scannerToParser);
        this.ast = (ProgramNode) p.parse().value;

        List<LexerError> lexerErrors = scannerToParser.getErrors();
        if (!lexerErrors.isEmpty()) {
            System.err.println("\n❌ Lexical Errors Found:");
            for (LexerError err : lexerErrors) {
                System.err.println(
                        "   - "
                                + err.getMessage()
                                + " na linha "
                                + err.getLine()
                                + ", coluna "
                                + err.getCol());
            }
            throw new RuntimeException("Lexical phase validation failed.");
        }
    }

    public ProgramNode getAST() {
        return ast;
    }

    public void debugScanner(Scanner scanner) throws Exception {
        java_cup.runtime.Symbol token;
        System.out.println("\n--- ---- Scanner phase --- ----");
        System.out.println("tokens ids and names");
        while ((token = scanner.next_token()).sym != EOF) {
            String name = terminalNames[token.sym];
            System.out.println("Token: " + name + " (ID: " + token.sym + ") value: " + token.value);
        }
    }
}
