package core.ast.nodes.literals;

import core.ast.ASTNode;
import core.ast.Visitor;

public class BooleanNode extends ASTNode {
    private Boolean value;

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
