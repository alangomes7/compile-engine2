package core.semantic;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    // A nossa Pilha de Escopos (o topo é o escopo atual, a base é o escopo global)
    private final Deque<Map<String, SymbolInfo>> scopes;

    public SymbolTable() {
        this.scopes = new ArrayDeque<>();
        // Assim que a tabela é instanciada, inicializamos o Escopo Global
        enterScope();
    }

    /**
     * Entra em um novo bloco (ex: inicia um if, lambda ou let). Empilha um novo HashMap na memória.
     */
    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    /** Sai de um bloco. Desempilha e destrói as variáveis locais daquele bloco. */
    public void exitScope() {
        if (scopes.size() > 1) {
            scopes.pop();
        } else {
            throw new RuntimeException("Erro Interno: Tentativa de destruir o Escopo Global.");
        }
    }

    /** Declara uma nova variável no escopo atual. */
    public void define(String name, SymbolInfo info) {
        Map<String, SymbolInfo> currentScope = scopes.peek();

        if (currentScope.containsKey(name)) {
            // Em Scheme puro, 'define' no mesmo escopo pode sobrescrever,
            // mas do ponto de vista de tipagem estrita pode ser um erro.
            // Para simplificar, vamos permitir sobrescrever/atualizar.
            currentScope.put(name, info);
        } else {
            currentScope.put(name, info);
        }
    }

    /** Procura uma variável. Olha primeiro no escopo local (topo) e vai descendo até o global. */
    public SymbolInfo lookup(String name) {
        for (Map<String, SymbolInfo> scope : scopes) {
            if (scope.containsKey(name)) {
                return scope.get(name); // Encontrou!
            }
        }
        return null; // A variável não existe em nenhum escopo acima.
    }
}
