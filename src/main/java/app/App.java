package app;

public class App {
    public static void main(String[] args) {
        /*
        String inputCode = "src/main/resources/jflex/example1.scm";

        Scanner scanner = null;
        try {
            scanner = new Scanner(Utils.readInputCode(inputCode));
            Token token = scanner.yylex();
            while (token != null) {
                System.out.println("Scanned Token: " + token.toString());
                token = scanner.yylex();
            }
            scanner.yyclose();

            System.out.println("\n--- SCANNING COMPLETE ---\n");
            SymbolTable symbolTable = scanner.getSymbolTable();
            symbolTable.printTable();

        } catch (IOException ex) {
            String errorMessage = "An IO Error occurred during scanning: " + ex.getMessage();
            if (scanner != null) {
                List<LexerError> errors = scanner.getErrors();
                System.out.println("Lexical Errors:");
                for (LexerError error : errors) {
                    errorMessage += "\n" + error.toString();
                }
            }
            System.err.println(errorMessage);
            System.getLogger(App.class.getName())
                    .log(System.Logger.Level.ERROR, (String) null, errorMessage);
        }
        */
    }
}
