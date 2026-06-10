package core.codegen;

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
import java.util.stream.Collectors;

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
        return node.getThenBranch().accept(this)
                + " if "
                + node.getCondition().accept(this)
                + " else "
                + (node.getElseBranch() != null ? node.getElseBranch().accept(this) : "None");
    }

    @Override
    public String visit(LambdaNode node) {
        StringBuilder params = new StringBuilder();
        List<IdentifierNode> parameters = node.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
            if (i > 0) params.append(", ");
            params.append(parameters.get(i).accept(this));
        }
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
        return sequenceToPython(node.getExpressions());
    }

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

    @Override
    public String visit(LetNode node) {
        StringBuilder params = new StringBuilder();
        StringBuilder args = new StringBuilder();

        List<BindingNode> bindings = node.getBindings();
        for (int i = 0; i < bindings.size(); i++) {
            if (i > 0) {
                params.append(", ");
                args.append(", ");
            }
            params.append(bindings.get(i).getVariable().accept(this));
            args.append(bindings.get(i).getExpression().accept(this));
        }

        String body = sequenceToPython(node.getBody());

        return "(lambda " + params + ": " + body + ")(" + args + ")";
    }

    @Override
    public String visit(BindingNode node) {
        return node.getVariable().accept(this) + " = " + node.getExpression().accept(this);
    }

    @Override
    public String visit(CondNode node) {
        return buildCondTernary(node.getClauses(), 0);
    }

    private String buildCondTernary(List<CondClauseNode> clauses, int index) {
        if (index >= clauses.size()) {
            return "None";
        }

        CondClauseNode clause = clauses.get(index);

        if (clause.isElseClause()) {
            return sequenceToPython(clause.getSequence());
        }

        String test = clause.getTest().accept(this);
        String thenBranch = sequenceToPython(clause.getSequence());
        String elseBranch = buildCondTernary(clauses, index + 1);

        return "(" + thenBranch + " if " + test + " else " + elseBranch + ")";
    }

    @Override
    public String visit(CondClauseNode node) {
        return "";
    }

    @Override
    public String visit(ListNode node) {
        List<String> elements = new ArrayList<>();
        for (ASTNode element : node.getElements()) {
            elements.add(element.accept(this));
        }
        return "[" + String.join(", ", elements) + "]";
    }

    @Override
    public String visit(AndNode node) {
        return "("
                + node.getExpressions().stream()
                        .map(e -> e.accept(this))
                        .collect(Collectors.joining(" and "))
                + ")";
    }

    @Override
    public String visit(OrNode node) {
        return "("
                + node.getExpressions().stream()
                        .map(e -> e.accept(this))
                        .collect(Collectors.joining(" and "))
                + ")";
    }
}
