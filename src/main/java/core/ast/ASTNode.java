package core.ast;

public abstract class ASTNode {
    private int line;
    private int column;

    public ASTNode(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    // Método obrigatório para o padrão Visitor (usaremos na Análise Semântica)
    // Todo nó que herdar de ASTNode terá que implementar esse método.
    public abstract <T> T accept(Visitor<T> visitor);
}
