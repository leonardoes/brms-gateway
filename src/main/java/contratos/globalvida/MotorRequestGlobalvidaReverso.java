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

    // Calculado e preenchido exclusivamente pelo gateway (lista cnaesRecusados.csv);
    // o motor apenas consome para aplicar a crítica de CNAE recusado.
    // String ("true"/"false") porque a ferramenta low-code não tem tipo booleano.
    private String cnaeRecusado;
}
