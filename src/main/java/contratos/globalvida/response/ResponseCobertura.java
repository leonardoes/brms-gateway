package contratos.globalvida.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import drools.RetornoRegra;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCobertura implements Serializable {
    private String codigoCobertura;
    private List<RetornoRegra> regras;
    private Double premio;
    private Double iof;
    private BigDecimal capitalCobertura;
    private Integer tipoCapital;
    private Double capitalTotal;
    private Double taxaPura;
}
