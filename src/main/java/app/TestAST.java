package app;

import core.lexer.Scanner;
import core.parser.ast.ASTPrinter;
import core.parser.ast.nodes.ProgramNode;
import core.parser.parser;
import java.io.StringReader;

public class TestAST {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        // Um código Scheme de teste que usa exatamente as regras que mapeamos na v3
        String codigoScheme =
                """
                              (define x 10)
                              (if #t x 20)""";

        try {
            System.out.println("=============================================");
            System.out.println("   INICIANDO TESTE DE CONSTRUÇÃO DA AST      ");
            System.out.println("=============================================");
            System.out.println("Código lido:\n" + codigoScheme + "\n");

            // 1. Inicializa o analisador léxico (Scanner) com a String de teste
            Scanner scanner = new Scanner(new StringReader(codigoScheme));

            // 2. Inicializa o parser sintático (fornecido pelo parser_v3.cup)
            parser p = new parser(scanner);

            // 3. Executa a análise sintática e recupera o objeto raiz da árvore
            System.out.println("-> Executando análise sintática...");
            ProgramNode raiz = (ProgramNode) p.parse().value;

            System.out.println("✅ Sintaxe válida! Árvore Sintática (AST) criada na memória.\n");

            // 4. Instancia o Visitor de impressão e faz a viagem pela árvore
            System.out.println("=== VISUALIZAÇÃO DA ÁRVORE SINTÁTICA (AST) ===");
            ASTPrinter printer = new ASTPrinter();
            raiz.accept(printer);
            System.out.println("=============================================");

        } catch (Exception e) {
            System.err.println("❌ Ocorreu um erro durante o processamento sintático:");
            e.printStackTrace();
        }
    }
}
