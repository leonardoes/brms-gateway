package contratos.globalvida.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Socio implements Serializable {
    private Integer numeroVidas;
    private Double capitalUniforme;
    @Valid
    private List<Cobertura> coberturas;
    @Valid
    private List<Assistencia> assistencias;
}
