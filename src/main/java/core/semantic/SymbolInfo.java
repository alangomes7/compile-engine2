package core.semantic;

public class SymbolInfo {
    private final String name;
    private SchemeType type;
    private final int line;
    private final int column;

    public SymbolInfo(String name, SchemeType type, int line, int column) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public SchemeType getType() {
        return type;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public void setType(SchemeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
