package core.semantic;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;
import core.parser.ast.nodes.BindingNode;
import core.parser.ast.nodes.CondClauseNode;
import core.parser.ast.nodes.CondNode;
import core.parser.ast.nodes.LetNode;
import core.parser.ast.nodes.ListNode;
import core.parser.ast.nodes.ProgramNode;
import core.parser.ast.nodes.expressions.AndNode;
import core.parser.ast.nodes.expressions.BeginNode;
import core.parser.ast.nodes.expressions.IfNode;
import core.parser.ast.nodes.expressions.LambdaNode;
import core.parser.ast.nodes.expressions.OrNode;
import core.parser.ast.nodes.expressions.ProcedureCallNode;
import core.parser.ast.nodes.literals.BooleanNode;
import core.parser.ast.nodes.literals.IdentifierNode;
import core.parser.ast.nodes.literals.NumberNode;
import core.parser.ast.nodes.literals.StringNode;
import core.parser.ast.nodes.statements.AssignmentNode;
import core.parser.ast.nodes.statements.DefineNode;
import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer implements Visitor<SchemeType> {

    private final SymbolTable symbolTable;
    private final List<String> errors;

    public SemanticAnalyzer() {
        this.symbolTable = new SymbolTable();
        this.errors = new ArrayList<>();
        // Built-ins do Scheme
        String[] builtins = {"car", "cdr", "cons", "null?", "list", "sqrt"};
        for (String func : builtins) {
            symbolTable.define(func, new SymbolInfo(func, SchemeType.FUNCTION, 0, 0));
        }
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public SchemeType visit(ProgramNode node) {
        for (ASTNode child : node.getCommandsOrDefinitions()) {
            child.accept(this);
        }
        return SchemeType.UNKNOWN;
    }

    @Override
    public SchemeType visit(ListNode node) {
        for (ASTNode element : node.getElements()) {
            element.accept(this);
        }
        return SchemeType.UNKNOWN;
    }

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

    @Override
    public SchemeType visit(IdentifierNode node) {
        SymbolInfo info = symbolTable.lookup(node.getName());
        if (info == null) {
            errors.add(
                    "Scope error [lin "
                            + node.getLine()
                            + "]: var '"
                            + node.getName()
                            + "' not declared.");
            return SchemeType.UNKNOWN;
        }
        return info.getType();
    }

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
            errors.add(
                    "Scope error [lin "
                            + node.getLine()
                            + "]: Not possible to re-assign to'"
                            + varName
                            + "', variable not declared.");
        } else {
            SchemeType newValueType = node.getValue().accept(this);
            info.setType(newValueType);
        }
        return SchemeType.UNKNOWN;
    }

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
                        errors.add(
                                "Type error [lin "
                                        + node.getLine()
                                        + "]: the operator '"
                                        + funcName
                                        + "' required NUMBER, but received "
                                        + opType
                                        + ".");
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

        for (ASTNode bodyNode : node.getBody()) {
            bodyNode.accept(this);
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

    @Override
    public SchemeType visit(AndNode node) {
        for (ASTNode test : node.getExpressions()) {
            test.accept(this);
        }
        return SchemeType.BOOLEAN;
    }

    @Override
    public SchemeType visit(OrNode node) {
        for (ASTNode test : node.getExpressions()) {
            test.accept(this);
        }
        return SchemeType.BOOLEAN;
    }

    @Override
    public SchemeType visit(LetNode node) {
        symbolTable.enterScope();

        for (BindingNode binding : node.getBindings()) {
            SchemeType valueType = binding.getExpression().accept(this);
            String varName = binding.getVariable().getName();
            symbolTable.define(
                    varName,
                    new SymbolInfo(varName, valueType, binding.getLine(), binding.getColumn()));
        }

        SchemeType lastType = SchemeType.UNKNOWN;
        for (ASTNode expr : node.getBody()) {
            lastType = expr.accept(this);
        }

        symbolTable.exitScope();
        return lastType;
    }

    @Override
    public SchemeType visit(CondNode node) {
        SchemeType returnType = null;

        for (CondClauseNode clause : node.getClauses()) {
            if (clause.getTest() != null) {
                clause.getTest().accept(this);
            }
            SchemeType clauseType = SchemeType.UNKNOWN;

            for (ASTNode expr : clause.getSequence()) {
                clauseType = expr.accept(this);
            }
            if (returnType == null) returnType = clauseType;
            else if (returnType != clauseType) returnType = SchemeType.UNKNOWN;
        }
        return returnType != null ? returnType : SchemeType.UNKNOWN;
    }

    @Override
    public SchemeType visit(CondClauseNode node) {
        return SchemeType.UNKNOWN;
    }

    @Override
    public SchemeType visit(BindingNode node) {
        return SchemeType.UNKNOWN;
    }
}
