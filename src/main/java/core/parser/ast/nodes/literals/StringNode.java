package core.parser.ast.nodes.literals;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;

public class StringNode extends ASTNode {
    private final String value;

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
