package core.ast.nodes.literals;

import core.ast.ASTNode;
import core.ast.Visitor;

public class NumberNode extends ASTNode {
    private Double value;

    public NumberNode(int line, int column, Double value) {
        super(line, column);
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
