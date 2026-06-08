package core.codegen;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PythonSanitizer {

    // Set of all Python reserved keywords to avoid naming collisions
    private static final Set<String> PYTHON_KEYWORDS =
            new HashSet<>(
                    Arrays.asList(
                            "False",
                            "None",
                            "True",
                            "and",
                            "as",
                            "assert",
                            "async",
                            "await",
                            "break",
                            "class",
                            "continue",
                            "def",
                            "del",
                            "elif",
                            "else",
                            "except",
                            "finally",
                            "for",
                            "from",
                            "global",
                            "if",
                            "import",
                            "in",
                            "is",
                            "lambda",
                            "nonlocal",
                            "not",
                            "or",
                            "pass",
                            "raise",
                            "return",
                            "try",
                            "while",
                            "with",
                            "yield"));

    /**
     * Sanitizes a Scheme identifier for Python use. 1. Maps Scheme-specific characters to
     * Python-safe equivalents. 2. Checks if the resulting name is a Python keyword.
     */
    public static String sanitize(String identifier) {
        String sanitized =
                identifier
                        .replace("-", "_") // window-width -> window_width
                        .replace("?", "_p") // null? -> null_p
                        .replace("!", "_bang") // set! -> set_bang
                        .replace("*", "_star") // mult* -> mult_star
                        .replace("+", "_plus"); // add+ -> add_plus

        // If the sanitized name is a Python keyword (e.g., 'lambda'), append an underscore
        if (PYTHON_KEYWORDS.contains(sanitized)) {
            return sanitized + "_";
        }

        return sanitized;
    }
}
