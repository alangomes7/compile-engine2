package core.ast.nodes.expressions;

import core.ast.ASTNode;
import core.ast.Visitor;
import java.util.List;

public class CondClauseNode extends ASTNode {
    private ASTNode condition; // Será null se for a cláusula 'else'
    private List<ASTNode> sequence;

    public CondClauseNode(int line, int column, ASTNode condition, List<ASTNode> sequence) {
        super(line, column);
        this.condition = condition;
        this.sequence = sequence;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public List<ASTNode> getSequence() {
        return sequence;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        throw new UnsupportedOperationException(
                "Cláusulas Cond devem ser acessadas diretamente pelo CondNode.");
    }
}
