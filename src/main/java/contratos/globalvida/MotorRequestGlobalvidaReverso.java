package contratos.globalvida;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

/**
 * Variáveis enviadas ao motor de cálculo reverso (/rest/calculoreverso).
 *
 * Os nomes dos campos correspondem exatamente às variáveis injetadas pela
 * plataforma low-code em CalculoReverso.java (premioDesejado, numeroVidas, cnae,
 * carregamento, periodicidadePagamento, dataCotacao, dataCriacaoCotacao).
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MotorRequestGlobalvidaReverso {

    private int periodicidadePagamento;
    private Integer numeroVidas;
    private Double premioDesejado;
    private String cnae;
    private String carregamento;
    private String dataCotacao;
    private String dataCriacaoCotacao;
}
