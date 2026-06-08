package core.parser.ast.nodes.literals;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;

public class NumberNode extends ASTNode {
    private final Double value;

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
