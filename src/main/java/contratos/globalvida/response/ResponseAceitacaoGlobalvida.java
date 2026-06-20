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
public class ResponseAceitacaoGlobalvida {
    private int periodicidadePagamento;
    private String cnae;
    private String carregamento;
    private List<RetornoRegra> regrasCotacaoFuncionario;
    private List<RetornoRegra> regrasCotacaoSocio;
}
