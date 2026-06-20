package contratos.globalvida.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import jakarta.validation.Valid;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestGlobalvida implements Serializable {
    private int periodicidadePagamento;
    private String cnae;
    private String carregamento;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String dataCotacao;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String dataCriacaoCotacao;
    private Double capitalGlobal;
    @Valid
    private Funcionario funcionarios;
    @Valid
    private Socio socios;

    // SQL Query mode - if populated, the endpoint acts as SQL executor
    @JsonIgnore
    private String sqlQuery;
    @JsonIgnore
    private Integer maxRows;
}
