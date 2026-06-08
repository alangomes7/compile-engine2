package core.parser.ast.nodes;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;
import java.util.List;

public class CondNode extends ASTNode {
    private final List<CondClauseNode> clauses;

    public CondNode(int start, int end, List<CondClauseNode> clauses) {
        super(start, end);
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
