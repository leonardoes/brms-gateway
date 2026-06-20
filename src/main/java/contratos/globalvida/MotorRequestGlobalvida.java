package contratos.globalvida;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MotorRequestGlobalvida {

    private int periodicidadePagamento;
    private Integer numeroVidas;
    private String codigoAssistencia;
    private String codigoCobertura;
    private Double capitalCobertura;
    private String cnae;
    private String carregamento;
    private String dataCotacao;
    private String dataCriacaoCotacao;
    private String listaCoberturas;
    private Double capitalUniforme;
    private Integer tipoCapital;
    private Integer tipoValor;
    private Double capitalBasica;
    private Double capitalGlobal;
    private Integer tipoCapitalBasica;

    @JsonIgnore
    public MotorRequestGlobalvida getCopy() {
        MotorRequestGlobalvida novo = new MotorRequestGlobalvida();
        novo.periodicidadePagamento = this.periodicidadePagamento;
        novo.numeroVidas = this.numeroVidas;
        novo.codigoAssistencia = this.codigoAssistencia;
        novo.codigoCobertura = this.codigoCobertura;
        novo.cnae = this.cnae;
        novo.carregamento = this.carregamento;
        novo.dataCotacao = this.dataCotacao;
        novo.dataCriacaoCotacao = this.dataCriacaoCotacao;
        novo.capitalCobertura = this.capitalCobertura;
        novo.capitalUniforme = this.capitalUniforme;
        novo.listaCoberturas = this.listaCoberturas;
        novo.tipoCapital = this.tipoCapital;
        novo.tipoValor = this.tipoValor;
        novo.capitalBasica = this.capitalBasica;
        novo.capitalGlobal = this.capitalGlobal;
        novo.tipoCapitalBasica = this.tipoCapitalBasica;
        return novo;
    }
}
