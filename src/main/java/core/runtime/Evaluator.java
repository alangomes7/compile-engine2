package core.runtime;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;
import core.parser.ast.nodes.BindingNode;
import core.parser.ast.nodes.CondClauseNode;
import core.parser.ast.nodes.CondNode;
import core.parser.ast.nodes.LetNode;
import core.parser.ast.nodes.ListNode;
import core.parser.ast.nodes.ProgramNode;
import core.parser.ast.nodes.expressions.BeginNode;
import core.parser.ast.nodes.expressions.IfNode;
import core.parser.ast.nodes.expressions.LambdaNode;
import core.parser.ast.nodes.expressions.ProcedureCallNode;
import core.parser.ast.nodes.literals.BooleanNode;
import core.parser.ast.nodes.literals.IdentifierNode;
import core.parser.ast.nodes.literals.NumberNode;
import core.parser.ast.nodes.literals.StringNode;
import core.parser.ast.nodes.statements.AssignmentNode;
import core.parser.ast.nodes.statements.DefineNode;
import java.util.ArrayList;
import java.util.List;

public class Evaluator implements Visitor<Object> {
    private Environment environment = new Environment();

    // --- Closure Class to handle functions ---
    private class Closure implements Procedure {
        private final LambdaNode node;
        private final Environment closureEnv;

        public Closure(LambdaNode node, Environment env) {
            this.node = node;
            this.closureEnv = env;
        }

        @Override
        public Object call(Evaluator evaluator, List<Object> args) {
            Environment localEnv = new Environment(closureEnv);
            List<IdentifierNode> params = node.getParameters();
            for (int i = 0; i < params.size(); i++) {
                localEnv.define(params.get(i).getName(), args.get(i));
            }
            // Temporarily swap environment
            Environment previous = evaluator.environment;
            evaluator.environment = localEnv;
            Object result = null;
            for (ASTNode expr : node.getBody()) result = expr.accept(evaluator);
            evaluator.environment = previous;
            return result;
        }
    }

    // --- Core Logic ---
    @Override
    public Object visit(DefineNode node) {
        Object value = node.getValue().accept(this);
        environment.define(node.getName().getName(), value);
        return value;
    }

    @Override
    public Object visit(AssignmentNode node) {
        Object value = node.getValue().accept(this);
        environment.assign(node.getName().getName(), value);
        return value;
    }

    @Override
    public Object visit(LambdaNode node) {
        return new Closure(node, environment);
    }

    @Override
    public Object visit(ProcedureCallNode node) {
        Object callable = node.getOperator().accept(this);
        List<Object> args = new ArrayList<>();
        for (ASTNode operand : node.getOperands()) args.add(operand.accept(this));

        if (callable instanceof Procedure procedure) return procedure.call(this, args);
        throw new RuntimeException("Attempted to call non-function object");
    }

    @Override
    public Object visit(LetNode node) {
        Environment localEnv = new Environment(environment);
        for (BindingNode b : node.getBindings()) {
            localEnv.define(b.getVariable().getName(), b.getExpression().accept(this));
        }
        Environment previous = this.environment;
        this.environment = localEnv;
        Object result = null;
        for (ASTNode expr : node.getBody()) result = expr.accept(this);
        this.environment = previous;
        return result;
    }

    @Override
    public Object visit(CondNode node) {
        for (CondClauseNode clause : node.getClauses()) {
            if (clause.isElseClause()) {
                Object result = null;
                for (ASTNode e : clause.getSequence()) result = e.accept(this);
                return result;
            }
            if (isTruthy(clause.getTest().accept(this))) {
                Object result = null;
                for (ASTNode e : clause.getSequence()) result = e.accept(this);
                return result;
            }
        }
        return null;
    }

    @Override
    public Object visit(BeginNode node) {
        Object result = null;
        for (ASTNode expr : node.getExpressions()) result = expr.accept(this);
        return result;
    }

    @Override
    public Object visit(BooleanNode node) {
        return node.getValue();
    }

    @Override
    public Object visit(StringNode node) {
        return node.getValue();
    }

    @Override
    public Object visit(NumberNode node) {
        return node.getValue();
    }

    @Override
    public Object visit(IdentifierNode node) {
        return environment.get(node.getName());
    }

    @Override
    public Object visit(ListNode node) {
        List<Object> results = new ArrayList<>();
        for (ASTNode e : node.getElements()) {
            results.add(e.accept(this));
        }
        return results;
    }

    // --- Unimplemented/Stubs for Visitor interface ---
    @Override
    public Object visit(ProgramNode node) {
        Object result = null;
        for (ASTNode child : node.getCommandsOrDefinitions()) {
            result = child.accept(this);
        }
        return result;
    }

    @Override
    public Object visit(IfNode node) {
        Object conditionResult = node.getCondition().accept(this);

        if (isTruthy(conditionResult)) {
            return node.getThenBranch().accept(this);
        } else {
            return node.getElseBranch() != null ? node.getElseBranch().accept(this) : null;
        }
    }

    @Override
    public Object visit(CondClauseNode node) {
        // This node is typically visited via the logic in CondNode's visit method.
        // If called directly, it indicates a structural issue.
        throw new UnsupportedOperationException(
                "CondClauseNode should be evaluated within CondNode.");
    }

    @Override
    public Object visit(BindingNode node) {
        // This node is typically visited via the logic in LetNode's visit method.
        throw new UnsupportedOperationException("BindingNode should be evaluated within LetNode.");
    }

    private boolean isTruthy(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean aBoolean) return aBoolean;
        return true;
    }
}
