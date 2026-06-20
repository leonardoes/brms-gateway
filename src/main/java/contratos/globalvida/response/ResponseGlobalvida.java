package contratos.globalvida.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import drools.RetornoRegra;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseGlobalvida {
    private Integer periodicidadePagamento;
    private String cnae;
    private String carregamento;
    private Double capitalGlobal;
    private List<RetornoRegra> regras;
    private ResponseFuncionario funcionarios;
    private ResponseSocio socios;
    private ResponsePremio premioFinal;
    private ResponsePremioPeriodo premioPeriodo;
}
