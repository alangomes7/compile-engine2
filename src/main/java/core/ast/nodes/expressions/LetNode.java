package core.ast.nodes.expressions;

import core.ast.ASTNode;
import core.ast.Visitor;
import java.util.List;

public class LetNode extends ASTNode {
    private List<BindingNode> bindings;
    private List<ASTNode> body;

    public LetNode(int line, int column, List<BindingNode> bindings, List<ASTNode> body) {
        super(line, column);
        this.bindings = bindings;
        this.body = body;
    }

    public List<BindingNode> getBindings() {
        return bindings;
    }

    public List<ASTNode> getBody() {
        return body;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
