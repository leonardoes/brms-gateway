package gateway.web.servicos.interfaces;

import gateway.web.config.CoberturaValidaValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CoberturaValidaValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CoberturaValida {
    String message() default "Código inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
