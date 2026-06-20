package drools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Retorno {

    private String rastreio;
    private String bom;
    private Object variaveis;
    private List<RetornoRegra> regras;
    private List<RetornoCalculo> calculos;
    private List<RetornoTabela> tabelas;

}
