package core.parser.ast.nodes.expressions;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;
import java.util.List;

public class AndNode extends ASTNode {
    private final List<ASTNode> expressions;

    public AndNode(int line, int col, List<ASTNode> exprs) {
        super(line, col);
        this.expressions = exprs;
    }

    public List<ASTNode> getExpressions() {
        return expressions;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
