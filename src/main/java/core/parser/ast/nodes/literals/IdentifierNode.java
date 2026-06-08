package core.parser.ast.nodes.literals;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;

public class IdentifierNode extends ASTNode {
    private final String name;

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
