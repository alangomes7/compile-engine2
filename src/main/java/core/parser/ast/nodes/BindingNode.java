package core.parser.ast.nodes;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;
import core.parser.ast.nodes.literals.IdentifierNode;

public class BindingNode extends ASTNode {
    private final IdentifierNode variable;
    private final ASTNode expression;

    public BindingNode(int start, int end, IdentifierNode variable, ASTNode expression) {
        super(start, end);
        this.variable = variable;
        this.expression = expression;
    }

    public IdentifierNode getVariable() {
        return variable;
    }

    public ASTNode getExpression() {
        return expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
