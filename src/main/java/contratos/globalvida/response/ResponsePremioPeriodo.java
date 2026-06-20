package contratos.globalvida.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponsePremioPeriodo implements Serializable {
    private String periodo;
    private Double premioTotal;
    private Double premioFuncionario;
    private Double premioSocio;
}
