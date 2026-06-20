package contratos.globalvida.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gateway.web.servicos.interfaces.CoberturaValida;
import lombok.Data;
import lombok.ToString;

import jakarta.validation.Valid;
import java.io.Serializable;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cobertura implements Serializable {
    @CoberturaValida
    private String codigoCobertura;
    private Double capitalCobertura;
    private Integer tipoCapital; // 1 - Global | 2 - Individual
    private Integer tipoValor; // 1 - Monetário | 2 - Percentual

    public Integer getTipoValor() {
        if (this.tipoValor == null) {
            return 1;
        }
        return tipoValor;
    }
}
