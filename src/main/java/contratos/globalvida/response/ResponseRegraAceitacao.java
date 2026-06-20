package contratos.globalvida.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseRegraAceitacao {
    private Long codigoRetorno;
    private String nomeAmigavel;
    private String devolutiva;
}
