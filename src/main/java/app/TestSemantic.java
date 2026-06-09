package app;

import core.ast.nodes.ProgramNode;
import core.lexer.Scanner;
import core.semantic.SemanticAnalyzer;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import scanner.parser; // Ajustado: o pacote scanner está na raiz junto com app e core!

public class TestSemantic {

    public static void main(String[] args) {
        System.out.println("=====================================================");
        System.out.println("   INICIANDO BATERIA DE TESTES SEMÂNTICOS (FILES)    ");
        System.out.println("=====================================================\n");

        // Lista com os caminhos dos arquivos de teste baseados na sua estrutura
        String[] arquivosExemplo = {
            "src/main/resources/examples/exemplo1_sucesso.scm",
            "src/main/resources/examples/exemplo2_escopo.scm",
            "src/main/resources/examples/exemplo3_tipagem.scm"
        };

        for (String caminhoArquivo : arquivosExemplo) {
            runTestFromFile(caminhoArquivo);
        }
    }

    /** Método que lê um arquivo .scm e roda o pipeline do compilador. */
    private static void runTestFromFile(String filePath) {
        System.out.println(">>> Processando arquivo: " + filePath);

        try {
            // 0. Lê o conteúdo do arquivo
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                System.err.println("❌ ERRO: Arquivo não encontrado no caminho especificado.\n");
                return;
            }
            String codigoScheme = Files.readString(path);

            // 1. Lexer e Parser (Análise Léxica e Sintática)
            Scanner scanner = new Scanner(new StringReader(codigoScheme));
            parser p = new parser(scanner);
            ProgramNode raiz = (ProgramNode) p.parse().value;

            // 2. Analisador Semântico (Verificação de Escopo e Tipagem)
            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
            raiz.accept(semanticAnalyzer);

            // Se chegou aqui, o código passou por todas as validações!
            System.out.println("✅ RESULTADO: Sucesso! Nenhuma infração semântica encontrada.\n");

        } catch (RuntimeException e) {
            // Captura erros semânticos (variáveis não declaradas, erros de tipo)
            System.err.println("❌ ERRO SEMÂNTICO: " + e.getMessage() + "\n");
        } catch (Exception e) {
            // Captura erros de sintaxe ou problemas de I/O
            System.err.println("⚠️ ERRO SINTÁTICO/SISTEMA: " + e.getMessage() + "\n");
        }

        // Pausa rápida para organizar o console (System.out vs System.err)
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
    }
}
