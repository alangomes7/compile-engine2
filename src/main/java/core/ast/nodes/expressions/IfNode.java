package core.ast.nodes.expressions;

import core.ast.ASTNode;
import core.ast.Visitor;

public class IfNode extends ASTNode {
    private ASTNode condition;
    private ASTNode thenBranch;
    private ASTNode elseBranch; // Pode ser null se o 'if' não tiver 'else'

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
