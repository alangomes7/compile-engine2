package core.ast;

import core.ast.nodes.*;
import core.ast.nodes.expressions.*;
import core.ast.nodes.literals.*;
import core.ast.nodes.statements.*;

public interface Visitor<T> {
    T visit(ProgramNode node);

    // Literais
    T visit(NumberNode node);

    T visit(StringNode node);

    T visit(BooleanNode node);

    T visit(IdentifierNode node);

    // Statements
    T visit(DefineNode node);

    T visit(AssignmentNode node);

    // Expressões
    T visit(IfNode node);

    T visit(LambdaNode node);

    T visit(ProcedureCallNode node);

    // Açúcar Sintático (Derivadas)
    T visit(BeginNode node);
}
