package core.parser.ast.nodes.literals;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;

public class BooleanNode extends ASTNode {
    private final Boolean value;

    public BooleanNode(int line, int column, Boolean value) {
        super(line, column);
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
