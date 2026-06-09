package core.ast.nodes.expressions;

import core.ast.ASTNode;
import core.ast.Visitor;
import core.ast.nodes.literals.IdentifierNode;

public class BindingNode extends ASTNode {
    private IdentifierNode variable;
    private ASTNode value;

    public BindingNode(int line, int column, IdentifierNode variable, ASTNode value) {
        super(line, column);
        this.variable = variable;
        this.value = value;
    }

    public IdentifierNode getVariable() {
        return variable;
    }

    public ASTNode getValue() {
        return value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        throw new UnsupportedOperationException(
                "Bindings devem ser acessados diretamente pelo LetNode.");
    }
}
