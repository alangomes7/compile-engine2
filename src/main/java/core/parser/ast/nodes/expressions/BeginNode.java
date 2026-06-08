package core.parser.ast.nodes.expressions;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;
import java.util.List;

public class BeginNode extends ASTNode {
    private final List<ASTNode> expressions;

    public BeginNode(int line, int column, List<ASTNode> expressions) {
        super(line, column);
        this.expressions = expressions;
    }

    public List<ASTNode> getExpressions() {
        return expressions;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
