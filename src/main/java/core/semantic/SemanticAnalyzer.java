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
        // Percorre todos os comandos. O tipo de retorno do programa não importa muito.
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
        // Primeiro, avaliamos a expressão para descobrir o tipo dela
        SchemeType valueType = node.getValue().accept(this);

        // Depois, salvamos na tabela de símbolos no escopo atual
        String varName = node.getName().getName();
        symbolTable.define(
                varName, new SymbolInfo(varName, valueType, node.getLine(), node.getColumn()));

        return SchemeType.UNKNOWN; // 'define' em si não retorna um valor utilizável matematicamente
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

        // Avalia o novo valor e atualiza o tipo na tabela de símbolos
        SchemeType newValueType = node.getValue().accept(this);
        info.setType(newValueType);

        return SchemeType.UNKNOWN;
    }

    // --- 5. CONTROLE DE FLUXO E OPERAÇÕES ---
    @Override
    public SchemeType visit(IfNode node) {
        // Avalia a condição (no Scheme, tudo que não é #f é verdadeiro, mas podemos ser flexíveis
        // ou rigorosos)
        node.getCondition().accept(this);

        SchemeType thenType = node.getThenBranch().accept(this);

        if (node.getElseBranch() != null) {
            SchemeType elseType = node.getElseBranch().accept(this);
            // Se os tipos dos dois blocos forem diferentes, retornamos UNKNOWN por precaução
            if (thenType != elseType) return SchemeType.UNKNOWN;
        }

        return thenType;
    }

    @Override
    public SchemeType visit(ProcedureCallNode node) {
        // Avalia o operador (qual função está sendo chamada?)
        // Como o JavaCUP mapeia operadores nativos (como + e -) como Identifiers, nós os
        // interceptamos aqui
        if (node.getOperator() instanceof IdentifierNode) {
            String funcName = ((IdentifierNode) node.getOperator()).getName();

            // Verificação de Tipos Estrita para funções matemáticas nativas!
            if (funcName.equals("+")
                    || funcName.equals("-")
                    || funcName.equals("*")
                    || funcName.equals("/")) {
                for (ASTNode operand : node.getOperands()) {
                    SchemeType opType = operand.accept(this);

                    // Ajuste aqui: aceitamos NUMBER e UNKNOWN (parâmetros de lambda)
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

        // Se for outra chamada de função, apenas avalia os operandos por enquanto
        for (ASTNode operand : node.getOperands()) {
            operand.accept(this);
        }
        return SchemeType.UNKNOWN;
    }

    @Override
    public SchemeType visit(LambdaNode node) {
        // --- MAGIA DO ESCOPO ACONTECENDO AQUI ---
        symbolTable.enterScope(); // Cria um novo escopo isolado para a função

        // Declara os parâmetros dentro deste novo escopo (eles nascem como UNKNOWN pois receberão
        // valores na chamada)
        for (IdentifierNode param : node.getParameters()) {
            symbolTable.define(
                    param.getName(),
                    new SymbolInfo(
                            param.getName(),
                            SchemeType.UNKNOWN,
                            param.getLine(),
                            param.getColumn()));
        }

        // Avalia o corpo da função (agora ele enxerga os parâmetros)
        SchemeType returnType = SchemeType.UNKNOWN;
        for (ASTNode bodyNode : node.getBody()) {
            returnType = bodyNode.accept(this);
        }

        symbolTable.exitScope(); // Destrói as variáveis locais! O escopo global volta a ser o
        // principal.
        // -----------------------------------------

        return SchemeType.FUNCTION;
    }

    @Override
    public SchemeType visit(BeginNode node) {
        SchemeType lastType = SchemeType.UNKNOWN;
        for (ASTNode expr : node.getExpressions()) {
            lastType = expr.accept(this);
        }
        return lastType; // O 'begin' retorna o valor da sua última instrução
    }
}
