package core.parser.ast;

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
    public Void visit(AssignmentNode node) {
        printIndent("AssignmentNode");
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
    public Void visit(BindingNode node) {
        printIndent("BindingNode");
        indentLevel++;
        node.getVariable().accept(this);
        node.getExpression().accept(this);
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(LetNode node) {
        printIndent("LetNode");
        indentLevel++;
        for (BindingNode b : node.getBindings()) b.accept(this);
        for (ASTNode expr : node.getBody()) expr.accept(this);
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(CondNode node) {
        printIndent("CondNode");
        indentLevel++;
        for (CondClauseNode clause : node.getClauses()) clause.accept(this);
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(CondClauseNode node) {
        printIndent("CondClauseNode");
        indentLevel++;
        if (node.getTest() != null) node.getTest().accept(this);
        for (ASTNode expr : node.getSequence()) expr.accept(this);
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(ListNode node) {
        printIndent("ListNode");
        indentLevel++;
        for (ASTNode element : node.getElements()) {
            element.accept(this);
        }
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(AndNode node) {
        printIndent("AndNode");
        indentLevel++;
        for (ASTNode expr : node.getExpressions()) {
            expr.accept(this);
        }
        indentLevel--;
        return null;
    }

    @Override
    public Void visit(OrNode node) {
        printIndent("OrNode");
        indentLevel++;
        for (ASTNode expr : node.getExpressions()) {
            expr.accept(this);
        }
        indentLevel--;
        return null;
    }
}
