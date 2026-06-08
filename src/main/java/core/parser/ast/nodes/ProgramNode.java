package core.parser.ast.nodes;

import core.parser.ast.ASTNode;
import core.parser.ast.Visitor;
import java.util.List;

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
