package core.parser.ast;

import core.parser.ast.nodes.BindingNode;
import core.parser.ast.nodes.CondClauseNode;
import core.parser.ast.nodes.CondNode;
import core.parser.ast.nodes.LetNode;
import core.parser.ast.nodes.ListNode;
import core.parser.ast.nodes.ProgramNode;
import core.parser.ast.nodes.expressions.AndNode;
import core.parser.ast.nodes.expressions.BeginNode;
import core.parser.ast.nodes.expressions.IfNode;
import core.parser.ast.nodes.expressions.LambdaNode;
import core.parser.ast.nodes.expressions.OrNode;
import core.parser.ast.nodes.expressions.ProcedureCallNode;
import core.parser.ast.nodes.literals.BooleanNode;
import core.parser.ast.nodes.literals.IdentifierNode;
import core.parser.ast.nodes.literals.NumberNode;
import core.parser.ast.nodes.literals.StringNode;
import core.parser.ast.nodes.statements.AssignmentNode;
import core.parser.ast.nodes.statements.DefineNode;

public interface Visitor<T> {
    T visit(ProgramNode node);

    T visit(LetNode node);

    T visit(CondNode node);

    T visit(CondClauseNode node);

    T visit(BindingNode node);

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

    T visit(ListNode node);

    T visit(AndNode node);

    T visit(OrNode node);

    // Açúcar Sintático (Derivadas)
    T visit(BeginNode node);
}
