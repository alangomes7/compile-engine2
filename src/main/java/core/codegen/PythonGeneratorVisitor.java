package core.codegen;

import java.util.List;

import core.ast.ASTNode;
import core.ast.Visitor;
import core.ast.nodes.ProgramNode;
import core.ast.nodes.expressions.BeginNode;
import core.ast.nodes.expressions.IfNode;
import core.ast.nodes.expressions.LambdaNode;
import core.ast.nodes.expressions.ProcedureCallNode;
import core.ast.nodes.literals.BooleanNode;
import core.ast.nodes.literals.IdentifierNode;
import core.ast.nodes.literals.NumberNode;
import core.ast.nodes.literals.StringNode;
import core.ast.nodes.statements.AssignmentNode;
import core.ast.nodes.statements.DefineNode;

// Scheme to Python 3 code generator
public class PythonGeneratorVisitor implements Visitor<String> {

    @Override
    public String visit(ProgramNode node) {
        StringBuilder sb = new StringBuilder();
        for (ASTNode child : node.getCommandsOrDefinitions()) {
            sb.append(child.accept(this)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String visit(DefineNode node) {
        return node.getName().accept(this) + " = " + node.getValue().accept(this);
    }

    @Override
    public String visit(IdentifierNode node) {
        return PythonSanitizer.sanitize(node.getName());
    }

    @Override
    public String visit(NumberNode node) {
        return node.getValue().toString();
    }

    @Override
    public String visit(BooleanNode node) {
        return node.getValue() ? "True" : "False";
    }

    @Override
    public String visit(StringNode node) {
        return "\"" + node.getValue() + "\"";
    }

    @Override
    public String visit(IfNode node) {
        // (if cond then else) -> (then if cond else else)
        return node.getThenBranch().accept(this)
                + " if "
                + node.getCondition().accept(this)
                + " else "
                + (node.getElseBranch() != null ? node.getElseBranch().accept(this) : "None");
    }

    @Override
    public String visit(LambdaNode node) {
        // Generate Python lambda: lambda params: body
        // Parameters list
        StringBuilder params = new StringBuilder();
        List<IdentifierNode> parameters = node.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
            if (i > 0) params.append(", ");
            params.append(parameters.get(i).accept(this));
        }
        // Body: convert sequence to a single Python expression
        String body = sequenceToPython(node.getBody());
        return "lambda " + params + ": " + body;
    }

    @Override
    public String visit(ProcedureCallNode node) {
        String operator = node.getOperator().accept(this);
        StringBuilder args = new StringBuilder();
        List<ASTNode> operands = node.getOperands();
        for (int i = 0; i < operands.size(); i++) {
            if (i > 0) args.append(", ");
            args.append(operands.get(i).accept(this));
        }
        return operator + "(" + args + ")";
    }

    @Override
    public String visit(AssignmentNode node) {
        return node.getName().accept(this) + " = " + node.getValue().accept(this);
    }

    @Override
    public String visit(BeginNode node) {
        // (begin expr1 expr2 ...) -> tuple indexing to return last value
        return sequenceToPython(node.getExpressions());
    }

    /**
     * Converts a sequence of Scheme expressions into a single Python expression. If only one
     * expression, returns it directly. Otherwise, returns (expr1, expr2, ..., exprN)[-1] which
     * evaluates all in order and yields the last one.
     */
    private String sequenceToPython(List<ASTNode> exprs) {
        if (exprs == null || exprs.isEmpty()) {
            return "None";
        }
        if (exprs.size() == 1) {
            return exprs.get(0).accept(this);
        }
        StringBuilder tuple = new StringBuilder("(");
        for (int i = 0; i < exprs.size(); i++) {
            if (i > 0) tuple.append(", ");
            tuple.append(exprs.get(i).accept(this));
        }
        tuple.append(")[-1]");
        return tuple.toString();
    }
}
