package core.ast;

import core.ast.nodes.*;
import core.ast.nodes.expressions.*;
import core.ast.nodes.literals.*;
import core.ast.nodes.statements.*;

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
        indentLevel++;
        node.getName().accept(this);
        node.getValue().accept(this);
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(LambdaNode node) {
        printIndent("LambdaNode");
        indentLevel++;
        for (IdentifierNode param : node.getParameters()) {
            param.accept(this);
        }
        for (ASTNode expr : node.getBody()) {
            expr.accept(this);
        }
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(ProcedureCallNode node) {
        printIndent("ProcedureCallNode");
        indentLevel++;
        node.getOperator().accept(this);
        for (ASTNode operand : node.getOperands()) {
            operand.accept(this);
        }
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(BeginNode node) {
        printIndent("BeginNode");
        indentLevel++;
        for (ASTNode expr : node.getExpressions()) {
            expr.accept(this);
        }
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(AndNode node) {
        printIndent("AndNode");
        indentLevel++;
        for (ASTNode test : node.getTests()) {
            test.accept(this);
        }
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(OrNode node) {
        printIndent("OrNode");
        indentLevel++;
        for (ASTNode test : node.getTests()) {
            test.accept(this);
        }
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(LetNode node) {
        printIndent("LetNode");
        indentLevel++;

        printIndent("Bindings:");
        indentLevel++;
        for (BindingNode binding : node.getBindings()) {
            printIndent("Binding");
            indentLevel++;
            binding.getVariable().accept(this);
            binding.getValue().accept(this);
            indentLevel--;
        }
        indentLevel--;

        printIndent("Body:");
        indentLevel++;
        for (ASTNode expr : node.getBody()) {
            expr.accept(this);
        }
        indentLevel--;

        indentLevel--;
        return null;
    }

    @Override
    public Void visit(CondNode node) {
        printIndent("CondNode");
        indentLevel++;

        for (CondClauseNode clause : node.getClauses()) {
            printIndent("CondClause");
            indentLevel++;

            if (clause.getCondition() != null) {
                clause.getCondition().accept(this);
            } else {
                printIndent("Else");
            }

            for (ASTNode expr : clause.getSequence()) {
                expr.accept(this);
            }
            indentLevel--;
        }
        indentLevel--;
        return null;
    }
}
