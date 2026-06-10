package core.semantic;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Deque<Map<String, SymbolInfo>> scopes;

    public SymbolTable() {
        this.scopes = new ArrayDeque<>();
        enterScope();
    }

    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    public void exitScope() {
        if (scopes.size() > 1) {
            scopes.pop();
        } else {
            throw new RuntimeException("Erro Interno: Tentativa de destruir o Escopo Global.");
        }
    }

    public void define(String name, SymbolInfo info) {
        Map<String, SymbolInfo> currentScope = scopes.peek();

        if (currentScope.containsKey(name)) {
            currentScope.put(name, info);
        } else {
            currentScope.put(name, info);
        }
    }

    public SymbolInfo lookup(String name) {
        for (Map<String, SymbolInfo> scope : scopes) {
            if (scope.containsKey(name)) {
                return scope.get(name); // Encontrou!
            }
        }
        return null;
    }
}
