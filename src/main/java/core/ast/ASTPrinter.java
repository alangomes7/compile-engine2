package core.ast;

import core.ast.nodes.*;
import core.ast.nodes.expressions.*;
import core.ast.nodes.literals.*;
import core.ast.nodes.statements.*;

// Este Visitor viaja pela árvore apenas imprimindo o nome dos nós com indentação
public class ASTPrinter implements Visitor<Void> {
    private int indentLevel = 0;

    private void printIndent(String text) {
        System.out.println("  ".repeat(indentLevel) + text);
    }

    @Override
    public Void visit(ProgramNode node) {
        printIndent("ProgramNode");
        indentLevel++;
        for (ASTNode child : node.getCommandsOrDefinitions()) {
            child.accept(this);
        }
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(DefineNode node) {
        printIndent("DefineNode");
        indentLevel++;
        node.getName().accept(this);
        node.getValue().accept(this);
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(IfNode node) {
        printIndent("IfNode");
        indentLevel++;
        node.getCondition().accept(this);
        node.getThenBranch().accept(this);
        if (node.getElseBranch() != null) {
            node.getElseBranch().accept(this);
        }
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(NumberNode node) {
        printIndent("NumberNode(" + node.getValue() + ")");
        return null;
    }

    @Override
    public Void visit(IdentifierNode node) {
        printIndent("IdentifierNode(" + node.getName() + ")");
        return null;
    }

    // --- Implementações vazias provisórias para os outros nós ---
    @Override
    public Void visit(StringNode node) {
        printIndent("StringNode(" + node.getValue() + ")");
        return null;
    }

    @Override
    public Void visit(BooleanNode node) {
        printIndent("BooleanNode(" + node.getValue() + ")");
        return null;
    }

    @Override
    public Void visit(AssignmentNode node) {
        printIndent("AssignmentNode");
        return null;
    }

    @Override
    public Void visit(LambdaNode node) {
        printIndent("LambdaNode");
        return null;
    }

    @Override
    public Void visit(ProcedureCallNode node) {
        printIndent("ProcedureCallNode");
        return null;
    }

    @Override
    public Void visit(BeginNode node) {
        printIndent("BeginNode");
        return null;
    }
}
