package drools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RetornoFormula {

    private String formula;
    private String resultado;
    private String devolutiva;
    private String erro;

}
