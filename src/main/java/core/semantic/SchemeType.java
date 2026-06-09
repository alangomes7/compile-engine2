package core.semantic;

public enum SchemeType {
    NUMBER,
    BOOLEAN,
    STRING,
    FUNCTION, // Para funções do 'define' e lambdas
    UNKNOWN // Para quando o tipo ainda não puder ser inferido ou der erro
}
