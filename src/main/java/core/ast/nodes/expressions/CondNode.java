package core.ast.nodes.expressions;

import core.ast.ASTNode;
import core.ast.Visitor;
import java.util.List;

public class CondNode extends ASTNode {
    private List<CondClauseNode> clauses;

    public CondNode(int line, int column, List<CondClauseNode> clauses) {
        super(line, column);
        this.clauses = clauses;
    }

    public List<CondClauseNode> getClauses() {
        return clauses;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
