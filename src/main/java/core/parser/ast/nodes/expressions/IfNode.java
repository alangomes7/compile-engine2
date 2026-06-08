package core.parser.ast.nodes.expressions;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;

public class IfNode extends ASTNode {
    private final ASTNode condition;
    private final ASTNode thenBranch;
    private final ASTNode elseBranch; // Pode ser null se o 'if' não tiver 'else'

    public IfNode(int line, int column, ASTNode condition, ASTNode thenBranch, ASTNode elseBranch) {
        super(line, column);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public ASTNode getThenBranch() {
        return thenBranch;
    }

    public ASTNode getElseBranch() {
        return elseBranch;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
