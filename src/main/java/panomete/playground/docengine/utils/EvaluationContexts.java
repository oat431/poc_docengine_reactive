package panomete.playground.docengine.utils;

import org.springframework.context.expression.MapAccessor;
import pro.verron.officestamper.api.EvaluationContextConfigurer;

public class EvaluationContexts {
    public static EvaluationContextConfigurer enableMapAccess() {
        return ctx -> ctx.addPropertyAccessor(new MapAccessor());
    }
}
