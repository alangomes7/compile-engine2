package core.parser.ast.nodes;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;
import java.util.List;

public class ListNode extends ASTNode {
    private final List<ASTNode> elements;

    public ListNode(List<ASTNode> elements) {
        super(0, 0);
        this.elements = elements;
    }

    public List<ASTNode> getElements() {
        return elements;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
