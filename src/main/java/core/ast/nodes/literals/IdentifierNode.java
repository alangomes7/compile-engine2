package core.ast.nodes.literals;

import core.ast.ASTNode;
import core.ast.Visitor;

public class IdentifierNode extends ASTNode {
    private String name;

    public IdentifierNode(int line, int column, String name) {
        super(line, column);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
