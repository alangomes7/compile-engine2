package core.ast.nodes.expressions;

import core.ast.ASTNode;
import core.ast.Visitor;
import java.util.List;

public class ProcedureCallNode extends ASTNode {
    private ASTNode operator;
    private List<ASTNode> operands;

    public ProcedureCallNode(int line, int column, ASTNode operator, List<ASTNode> operands) {
        super(line, column);
        this.operator = operator;
        this.operands = operands;
    }

    public ASTNode getOperator() {
        return operator;
    }

    public List<ASTNode> getOperands() {
        return operands;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
