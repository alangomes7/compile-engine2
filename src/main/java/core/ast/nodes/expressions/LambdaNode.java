package core.ast.nodes.expressions;

import java.util.List;

import core.ast.ASTNode;
import core.ast.Visitor;
import core.ast.nodes.literals.IdentifierNode;

public class LambdaNode extends ASTNode {
    private final List<IdentifierNode> parameters;
    private final List<ASTNode> body;

    public LambdaNode(int line, int column, List<IdentifierNode> parameters, List<ASTNode> body) {
        super(line, column);
        this.parameters = parameters;
        this.body = body;
    }

    public List<IdentifierNode> getParameters() {
        return parameters;
    }

    public List<ASTNode> getBody() {
        return body;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
