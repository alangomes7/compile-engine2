package core.parser.ast.nodes.expressions;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;
import core.parser.ast.nodes.literals.IdentifierNode;
import java.util.List;

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
