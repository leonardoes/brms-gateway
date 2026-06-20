package drools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RetornoCalculo {

    private Long codigoRetorno;
    private String nomeAmigavel;
    private String devolutiva;
    private List<RetornoFormula> formulas;

}
