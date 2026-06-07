package core.ast.nodes.literals;

import core.ast.ASTNode;
import core.ast.Visitor;

public class StringNode extends ASTNode {
    private String value;

    public StringNode(int line, int column, String value) {
        super(line, column);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
