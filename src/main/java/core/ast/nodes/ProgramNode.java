package core.ast.nodes;

import java.util.List;

import core.ast.ASTNode;
import core.ast.Visitor;

public class ProgramNode extends ASTNode {
    private final List<ASTNode> commandsOrDefinitions;

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
