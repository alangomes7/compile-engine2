package core.semantic;

import core.ast.*;
import core.ast.Visitor;
import core.ast.nodes.*;
import core.ast.nodes.expressions.*;
import core.ast.nodes.literals.*;
import core.ast.nodes.statements.*;

public class SemanticAnalyzer implements Visitor<SchemeType> {

    private SymbolTable symbolTable;

    public SemanticAnalyzer() {
        this.symbolTable = new SymbolTable();
    }

    // --- 1. RAIZ DO PROGRAMA ---
    @Override
    public SchemeType visit(ProgramNode node) {
        for (ASTNode child : node.getCommandsOrDefinitions()) {
            child.accept(this);
        }
        return SchemeType.UNKNOWN;
    }

    // --- 2. VALORES LITERAIS (Folhas da Árvore) ---
    @Override
    public SchemeType visit(NumberNode node) {
        return SchemeType.NUMBER;
    }

    @Override
    public SchemeType visit(StringNode node) {
        return SchemeType.STRING;
    }

    @Override
    public SchemeType visit(BooleanNode node) {
        return SchemeType.BOOLEAN;
    }

    // --- 3. IDENTIFICADORES E CONTEXTO ---
    @Override
    public SchemeType visit(IdentifierNode node) {
        SymbolInfo info = symbolTable.lookup(node.getName());
        if (info == null) {
            throw new RuntimeException(
                    "Erro Semântico [Linha "
                            + node.getLine()
                            + "]: Variável '"
                            + node.getName()
                            + "' não foi declarada neste escopo.");
        }
        return info.getType();
    }

    // --- 4. DECLARAÇÕES E ATRIBUIÇÕES ---
    @Override
    public SchemeType visit(DefineNode node) {
        SchemeType valueType = node.getValue().accept(this);
        String varName = node.getName().getName();
        symbolTable.define(
                varName, new SymbolInfo(varName, valueType, node.getLine(), node.getColumn()));
        return SchemeType.UNKNOWN;
    }

    @Override
    public SchemeType visit(AssignmentNode node) {
        String varName = node.getName().getName();
        SymbolInfo info = symbolTable.lookup(varName);

        if (info == null) {
            throw new RuntimeException(
                    "Erro Semântico [Linha "
                            + node.getLine()
                            + "]: Não é possível reatribuir (set!) a variável '"
                            + varName
                            + "', pois ela não existe.");
        }

        SchemeType newValueType = node.getValue().accept(this);
        info.setType(newValueType);
        return SchemeType.UNKNOWN;
    }

    // --- 5. CONTROLE DE FLUXO E OPERAÇÕES ---
    @Override
    public SchemeType visit(IfNode node) {
        node.getCondition().accept(this);
        SchemeType thenType = node.getThenBranch().accept(this);

        if (node.getElseBranch() != null) {
            SchemeType elseType = node.getElseBranch().accept(this);
            if (thenType != elseType) return SchemeType.UNKNOWN;
        }
        return thenType;
    }

    @Override
    public SchemeType visit(ProcedureCallNode node) {
        if (node.getOperator() instanceof IdentifierNode) {
            String funcName = ((IdentifierNode) node.getOperator()).getName();

            if (funcName.equals("+")
                    || funcName.equals("-")
                    || funcName.equals("*")
                    || funcName.equals("/")) {
                for (ASTNode operand : node.getOperands()) {
                    SchemeType opType = operand.accept(this);
                    if (opType != SchemeType.NUMBER && opType != SchemeType.UNKNOWN) {
                        throw new RuntimeException(
                                "Erro Semântico de Tipagem [Linha "
                                        + node.getLine()
                                        + "]: A operação '"
                                        + funcName
                                        + "' espera receber apenas NUMBER, mas recebeu "
                                        + opType);
                    }
                }
                return SchemeType.NUMBER;
            }
        }

        for (ASTNode operand : node.getOperands()) {
            operand.accept(this);
        }
        return SchemeType.UNKNOWN;
    }

    @Override
    public SchemeType visit(LambdaNode node) {
        symbolTable.enterScope();

        for (IdentifierNode param : node.getParameters()) {
            symbolTable.define(
                    param.getName(),
                    new SymbolInfo(
                            param.getName(),
                            SchemeType.UNKNOWN,
                            param.getLine(),
                            param.getColumn()));
        }

        SchemeType returnType = SchemeType.UNKNOWN;
        for (ASTNode bodyNode : node.getBody()) {
            returnType = bodyNode.accept(this);
        }

        symbolTable.exitScope();
        return SchemeType.FUNCTION;
    }

    @Override
    public SchemeType visit(BeginNode node) {
        SchemeType lastType = SchemeType.UNKNOWN;
        for (ASTNode expr : node.getExpressions()) {
            lastType = expr.accept(this);
        }
        return lastType;
    }

    // ==========================================
    // --- NOVOS NÓS DE AÇÚCAR SINTÁTICO ---
    // ==========================================

    @Override
    public SchemeType visit(AndNode node) {
        for (ASTNode test : node.getTests()) {
            test.accept(this); // Apenas avalia se os nós internos são semanticamente válidos
        }
        return SchemeType.BOOLEAN;
    }

    @Override
    public SchemeType visit(OrNode node) {
        for (ASTNode test : node.getTests()) {
            test.accept(this);
        }
        return SchemeType.BOOLEAN;
    }

    @Override
    public SchemeType visit(LetNode node) {
        // O let cria um escopo local para suas variáveis
        symbolTable.enterScope();

        // 1. Resolvemos os valores das variáveis e as registramos na Tabela de Símbolos
        for (BindingNode binding : node.getBindings()) {
            SchemeType valueType = binding.getValue().accept(this);
            String varName = binding.getVariable().getName();

            symbolTable.define(
                    varName,
                    new SymbolInfo(varName, valueType, binding.getLine(), binding.getColumn()));
        }

        // 2. Avaliamos o corpo do Let (que agora enxerga essas variáveis recém-criadas)
        SchemeType lastType = SchemeType.UNKNOWN;
        for (ASTNode expr : node.getBody()) {
            lastType = expr.accept(this);
        }

        // 3. Destruímos o escopo! As variáveis do let deixam de existir aqui.
        symbolTable.exitScope();

        return lastType; // O let retorna o tipo da sua última expressão executada
    }

    @Override
    public SchemeType visit(CondNode node) {
        SchemeType returnType = null;

        for (CondClauseNode clause : node.getClauses()) {
            // Se não for o 'else', avalia a condição
            if (clause.getCondition() != null) {
                clause.getCondition().accept(this);
            }

            // Avalia os comandos executados caso essa condição seja verdadeira
            SchemeType clauseType = SchemeType.UNKNOWN;
            for (ASTNode expr : clause.getSequence()) {
                clauseType = expr.accept(this);
            }

            // Se for a primeira cláusula, salvamos o tipo.
            // Se as cláusulas retornarem tipos muito diferentes, marcamos como UNKNOWN.
            if (returnType == null) {
                returnType = clauseType;
            } else if (returnType != clauseType) {
                returnType = SchemeType.UNKNOWN;
            }
        }

        return returnType != null ? returnType : SchemeType.UNKNOWN;
    }
}
