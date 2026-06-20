package gateway.web.config;

import gateway.web.servicos.interfaces.CoberturaValida;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class CoberturaValidaValidator implements ConstraintValidator<CoberturaValida, String> {

    private final List<String> codigosValidos = Arrays.asList(
            "13281", "13293", "13289", "13290", "13282", "13288", "13291", "13302",
            "13295", "13301", "13296", "13298", "13299", "13303", "13300", "13284",
            "13286", "16775", "17738", "17739", "17740", "17741", "16772", "16773",
            "16788", "17742", "17743", "17744", "17745", "13348", "13305", "13304",
            "16841", "16683", "18442", "16682", "18732", "18730", "18731", "13297",
            "13292"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || !codigosValidos.contains(value)) {
            context.disableDefaultConstraintViolation(); //Desabilita mensagem padrão
            context.buildConstraintViolationWithTemplate("Código de cobertura inválido " + value)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
