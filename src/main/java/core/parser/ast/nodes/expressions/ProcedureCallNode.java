package core.parser.ast.nodes.expressions;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;
import java.util.List;

public class ProcedureCallNode extends ASTNode {
    private final ASTNode operator;
    private final List<ASTNode> operands;

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
