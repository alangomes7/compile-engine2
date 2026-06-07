package core.ast.nodes.statements;

import core.ast.ASTNode;
import core.ast.Visitor;
import core.ast.nodes.literals.IdentifierNode;

public class DefineNode extends ASTNode {
    private IdentifierNode name;
    private ASTNode value;

    public DefineNode(int line, int column, IdentifierNode name, ASTNode value) {
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
