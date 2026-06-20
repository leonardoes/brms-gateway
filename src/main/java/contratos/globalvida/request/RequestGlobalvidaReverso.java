package contratos.globalvida.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Request de entrada do cálculo reverso Globalvida (POST /rest/calculoGlobalvidaReverso).
 * Espelha contrato/request.json do motor (brms-rules-globalvida-cotacao/calculo).
 *
 * As coberturas das três opções são fixas e montadas dentro do motor; por isso
 * o request só carrega os parâmetros do cálculo (premioDesejado, numeroVidas, etc.).
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestGlobalvidaReverso implements Serializable {

    private int periodicidadePagamento;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String dataCotacao;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String dataCriacaoCotacao;
    private String cnae;
    private String carregamento;
    private Integer numeroVidas;
    private Double premioDesejado;

    // Recebido no contrato, porém não utilizado no cálculo reverso atual (ver BACKLOG.md do motor).
    private List<Object> assistencias;
}
