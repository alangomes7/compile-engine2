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
        System.out.println("   INICIANDO BATERIA DE TESTES SEMÂNTICOS (FILES)    ");
        System.out.println("=====================================================\n");

        // 2. Aponta para a nova pasta de testes do projeto
        Path testDir = Paths.get("src/test/schemeTests");

        if (!Files.exists(testDir)) {
            System.err.println(
                    "ERRO: O diretório de testes não foi encontrado em: "
                            + testDir.toAbsolutePath());
            return;
        }

        // 3. Lê automaticamente todos os arquivos .txt dentro da pasta
        try (Stream<Path> paths = Files.list(testDir)) {
            List<Path> files =
                    paths.filter(Files::isRegularFile)
                            .filter(p -> p.toString().endsWith(".txt"))
                            .sorted() // Garante que os arquivos sejam testados em ordem
                            .collect(Collectors.toList());

            for (Path filePath : files) {
                runTestFromFile(filePath.toString());
            }
        } catch (Exception e) {
            System.err.println("Erro ao ler o diretório de testes: " + e.getMessage());
        }
    }

    /** Método que lê um arquivo .txt e roda o pipeline do compilador. */
    private static void runTestFromFile(String filePath) {
        System.out.println(">>> Processando arquivo: " + filePath);

        try {
            // Passo 0: Lê o conteúdo do arquivo
            Path path = Paths.get(filePath);
            String codigoScheme = Files.readString(path);

            // Passo 1: Lexer e Parser (Análise Léxica e Sintática)
            Scanner scanner = new Scanner(new StringReader(codigoScheme));
            parser p = new parser(scanner);
            ProgramNode raiz = (ProgramNode) p.parse().value;

            // Passo 2: Analisador Semântico (Verificação de Escopo e Tipagem)
            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
            raiz.accept(semanticAnalyzer);

            // Passo 3: Avalia o relatório de erros (A mágica do acúmulo de erros aqui!)
            if (semanticAnalyzer.hasErrors()) {
                System.err.println("FORAM ENCONTRADOS ERROS SEMÂNTICOS:");
                for (String erro : semanticAnalyzer.getErrors()) {
                    System.err.println("   - " + erro);
                }
                System.err.println(); // Linha em branco para separar visualmente do próximo teste
            } else {
                System.out.println("RESULTADO: Sucesso! Nenhuma infração semântica encontrada.\n");
            }

        } catch (Exception e) {
            // Captura erros estruturais (Sintaxe inválida detectada pelo JCUP)
            System.err.println("ERRO SINTÁTICO/SISTEMA: " + e.getMessage() + "\n");
        }

        // Pausa rápida para organizar a concorrência do console (System.out vs System.err)
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
    }
}
