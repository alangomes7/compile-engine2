package core.runtime;

import java.util.List;

public interface Procedure {
    Object call(Evaluator evaluator, List<Object> arguments);
}
