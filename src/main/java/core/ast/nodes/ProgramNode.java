package core.ast.nodes;

import core.ast.ASTNode;
import core.ast.Visitor;
import java.util.List;

public class ProgramNode extends ASTNode {
    private List<ASTNode> commandsOrDefinitions;

    public ProgramNode(int line, int column, List<ASTNode> commandsOrDefinitions) {
        super(line, column);
        this.commandsOrDefinitions = commandsOrDefinitions;
    }

    public List<ASTNode> getCommandsOrDefinitions() {
        return commandsOrDefinitions;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
