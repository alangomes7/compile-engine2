package core.parser.ast.nodes.statements;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;
import core.parser.ast.nodes.literals.IdentifierNode;

public class AssignmentNode extends ASTNode {
    private final IdentifierNode name;
    private final ASTNode value;

    public AssignmentNode(int line, int column, IdentifierNode name, ASTNode value) {
        super(line, column);
        this.name = name;
        this.value = value;
    }

    public IdentifierNode getName() {
        return name;
    }

    public ASTNode getValue() {
        return value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
