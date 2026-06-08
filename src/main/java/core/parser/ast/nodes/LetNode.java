package core.parser.ast.nodes;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;
import java.util.List;

public class LetNode extends ASTNode {
    private final List<BindingNode> bindings;
    private final List<ASTNode> body;

    public LetNode(int start, int end, List<BindingNode> bindings, List<ASTNode> body) {
        super(start, end);
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
