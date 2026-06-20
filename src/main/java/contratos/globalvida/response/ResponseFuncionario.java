package contratos.globalvida.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import drools.RetornoRegra;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFuncionario implements Serializable {
    private Integer numeroVidas;
    private Double capitalUniforme;
    private List<RetornoRegra> regras;
    private List<ResponseCobertura> coberturas;
    private List<ResponseAssistencia> assistencias;
}
