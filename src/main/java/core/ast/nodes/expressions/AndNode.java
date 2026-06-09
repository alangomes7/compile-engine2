package core.ast.nodes.expressions;

import core.ast.ASTNode;
import core.ast.Visitor;
import java.util.List;

public class AndNode extends ASTNode {
    private List<ASTNode> tests;

    public AndNode(int line, int column, List<ASTNode> tests) {
        super(line, column);
        this.tests = tests;
    }

    public List<ASTNode> getTests() {
        return tests;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
