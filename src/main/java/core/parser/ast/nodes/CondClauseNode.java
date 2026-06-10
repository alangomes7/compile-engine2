package core.parser.ast.nodes;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;
import java.util.List;

public class CondClauseNode extends ASTNode {
    private final ASTNode test;
    private final List<ASTNode> sequence;

    public CondClauseNode(int start, int end, ASTNode test, List<ASTNode> sequence) {
        super(start, end);
        this.test = test;
        this.sequence = sequence;
    }

    public ASTNode getTest() {
        return test;
    }

    public List<ASTNode> getSequence() {
        return sequence;
    }

    public boolean isElseClause() {
        return test == null;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
