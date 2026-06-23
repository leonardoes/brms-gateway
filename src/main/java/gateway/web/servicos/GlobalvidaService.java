package gateway.web.servicos;

import contratos.RequestRegra;
import contratos.RequestTipo;
import contratos.globalvida.MotorRequestGlobalvida;
import contratos.globalvida.MotorRequestGlobalvidaReverso;
import contratos.globalvida.request.Assistencia;
import contratos.globalvida.request.Cobertura;
import contratos.globalvida.request.RequestGlobalvida;
import contratos.globalvida.request.RequestGlobalvidaReverso;
import contratos.globalvida.response.*;
import drools.Retorno;
import drools.RetornoRegra;
import gateway.web.config.CallEngine;
import gateway.web.config.CnaeRecusadoLoader;
import gateway.web.dto.SqlQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Validated
@RestController
@RequestMapping("rest")
@CrossOrigin
@Slf4j
public class GlobalvidaService {
    @Value("${app.brms.calculocotacao}")
    private String url_calculocotacao;
    @Value("${app.brms.calculoassistencia}")
    private String url_calculoassistencia;
    @Value("${app.brms.regraaceitacao}")
    private String url_regraaceitacao;
    @Value("${app.brms.calculoreverso}")
    private String url_calculoreverso;

    @Autowired
    private CallEngine callEngine;

    @Autowired
    private DroolsService droolsService;

    /*@Autowired
    private SqlQueryService sqlQueryService;*/

    private static final String msgErrMotor = "request-Motor: ";

    /**
     * Método utilitário para verificar e tratar valores NaN
     */
    private Double parseDoubleValue(String value) {
        if (value == null || value.trim().isEmpty() || "NaN".equalsIgnoreCase(value.trim())) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.warn("Erro ao converter valor numérico: {}, retornando 0.0", value);
            return 0.0;
        }
    }

    @RequestMapping(value = "calculoGlobalvida", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object calculoGlobalvida(@Valid @RequestBody RequestGlobalvida requestGlobalvida) throws Exception {
        return request(requestGlobalvida);
    }

    /**
     * Cálculo reverso: dado o premioDesejado, aciona o motor que devolve as três
     * opções com seus capitais. O corpo do motor é repassado verbatim ao cliente
     * (o motor entrega o JSON final, igual ao response de CalculoReverso.java).
     */
    @RequestMapping(value = "calculoGlobalvidaReverso", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object calculoGlobalvidaReverso(@Valid @RequestBody RequestGlobalvidaReverso requestReverso) throws Exception {
        return requestReverso(requestReverso);
    }

    /**
     * Monta as variáveis do motor de cálculo reverso e aciona o engine.
     */
    private Object requestReverso(final RequestGlobalvidaReverso requestReverso) throws Exception {
        MotorRequestGlobalvidaReverso variaveis = new MotorRequestGlobalvidaReverso();

        variaveis.setPeriodicidadePagamento(requestReverso.getPeriodicidadePagamento());
        variaveis.setNumeroVidas(requestReverso.getNumeroVidas());
        variaveis.setPremioDesejado(requestReverso.getPremioDesejado());
        variaveis.setCnae(requestReverso.getCnae());
        variaveis.setCarregamento(requestReverso.getCarregamento());
        // Único dado de regra que vive no gateway; sempre definido aqui (cliente não envia)
        variaveis.setCnaeRecusado(String.valueOf(CnaeRecusadoLoader.isCnaeRecusado(requestReverso.getCnae())));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if (requestReverso.getDataCotacao() != null) {
            variaveis.setDataCotacao(dateFormat.format(dateFormat.parse(requestReverso.getDataCotacao())));
        } else {
            variaveis.setDataCotacao(dateFormat.format(new Date()));
        }
        if (requestReverso.getDataCriacaoCotacao() != null) {
            variaveis.setDataCriacaoCotacao(dateFormat.format(dateFormat.parse(requestReverso.getDataCriacaoCotacao())));
        } else {
            variaveis.setDataCriacaoCotacao(dateFormat.format(new Date()));
        }

        RequestRegra<MotorRequestGlobalvidaReverso> motor = new RequestRegra<>();
        motor.setFiltrar(RequestTipo.TUDO);
        motor.setVariaveis(variaveis);

        // O motor embrulha a resposta em procedimentos[0].raiz; ao cliente devolvemos
        // somente o conteúdo de raiz, com as coberturas convertidas de sigla -> código.
        Object envelope = callEngine.runProcedimento(url_calculoreverso, motor);
        Object raiz = extrairRaizProcedimento(envelope);
        return CoberturaConverter.converterResponseReverso(raiz);
    }

    /**
     * Extrai o conteúdo de procedimentos[0].raiz do envelope retornado pelo motor.
     * Caso a estrutura não seja a esperada, devolve o envelope como veio (fallback).
     */
    @SuppressWarnings("unchecked")
    private Object extrairRaizProcedimento(Object envelope) {
        if (!(envelope instanceof Map)) return envelope;

        Object procedimentos = ((Map<String, Object>) envelope).get("procedimentos");
        if (procedimentos instanceof List && !((List<Object>) procedimentos).isEmpty()) {
            Object primeiro = ((List<Object>) procedimentos).get(0);
            if (primeiro instanceof Map) {
                Object raiz = ((Map<String, Object>) primeiro).get("raiz");
                if (raiz != null) return raiz;
            }
        }
        return envelope;
    }

    /**
     * Metodo para acionar motor de regras
     */
    private void regraaceitacao(RequestGlobalvida requestGlobalvida, ResponseGlobalvida responseGlobalvida) throws Exception {
        MotorRequestGlobalvida bom = new MotorRequestGlobalvida();

        bom.setPeriodicidadePagamento(requestGlobalvida.getPeriodicidadePagamento());
        bom.setCnae(requestGlobalvida.getCnae());
        bom.setCarregamento(requestGlobalvida.getCarregamento());

        String listaCoberturas = "";
        RequestRegra<MotorRequestGlobalvida> motorCotacao = new RequestRegra<>();
        Retorno retorno = null;

        List<RetornoRegra> listaRetornoRegra = new ArrayList<>();
        // Fire vidas limit rules first
        try {
            droolsService.fireRulesVidasLimit(requestGlobalvida, listaRetornoRegra);
        } catch (Throwable t) {
            log.warn("Falha ao executar regras de limite de vidas (Funcionarios)", t);
        }

        // REGRA CNAES RECUSADOS
        if (CnaeRecusadoLoader.isCnaeRecusado(requestGlobalvida.getCnae())) {
            RetornoRegra responseRegra = new RetornoRegra();
            responseRegra.setCodigoRetorno(-1L);
            responseRegra.setNomeAmigavel("CNAE Restrito");
            responseRegra.setDevolutiva("Cotação não permitida para esta Atividade Econômica.");
            listaRetornoRegra.add(responseRegra);
        }


        /* --------------------------
         * APLICAR REGRAS FUNCIONARIO
         * --------------------------
         */
        if (requestGlobalvida.getFuncionarios() != null) {
            bom.setNumeroVidas(requestGlobalvida.getFuncionarios().getNumeroVidas());
            for (Cobertura cobertura : requestGlobalvida.getFuncionarios().getCoberturas())
                listaCoberturas = listaCoberturas.concat(cobertura.getCodigoCobertura().concat("@"));
            bom.setListaCoberturas(listaCoberturas);
            motorCotacao.setFiltrar(RequestTipo.TUDO);
            motorCotacao.setVariaveis(bom);
            List<RetornoRegra> listaRetornoRegraFuncionario = new ArrayList<>();

            // Fire local Drools rules (Funcionarios)
            try {
                droolsService.fireRulesFuncionarios(requestGlobalvida, listaRetornoRegraFuncionario);
            } catch (Throwable t) {
                log.warn("Falha ao executar regras locais (Funcionarios)", t);
            }

            if (!listaRetornoRegraFuncionario.isEmpty()) {
                if (responseGlobalvida.getFuncionarios() == null) {
                    responseGlobalvida.setFuncionarios(new ResponseFuncionario());
                }
                responseGlobalvida.getFuncionarios().setRegras(listaRetornoRegraFuncionario);
            }
        }
        /* --------------------------
         * APLICAR REGRAS SOCIOS
         * --------------------------
         */
        listaCoberturas = "";

        if (requestGlobalvida.getSocios() != null) {
            bom.setNumeroVidas(requestGlobalvida.getSocios().getNumeroVidas());
            bom.setCapitalUniforme(requestGlobalvida.getSocios().getCapitalUniforme());
            for (Cobertura cobertura : requestGlobalvida.getSocios().getCoberturas())
                listaCoberturas = listaCoberturas.concat(cobertura.getCodigoCobertura().concat("@"));
            bom.setListaCoberturas(listaCoberturas);
            motorCotacao.setFiltrar(RequestTipo.TUDO);
            motorCotacao.setVariaveis(bom);
            List<RetornoRegra> listaRetornoRegraSocio = new ArrayList<>();

            // Fire local Drools rules (Socios)
            try {
                droolsService.fireRulesSocios(requestGlobalvida, listaRetornoRegraSocio);
            } catch (Throwable t) {
                log.warn("Falha ao executar regras locais (Socios)", t);
            }

            if (!listaRetornoRegraSocio.isEmpty()) {
                if (responseGlobalvida.getSocios() == null) {
                    responseGlobalvida.setSocios(new ResponseSocio());
                }
                responseGlobalvida.getSocios().setRegras(listaRetornoRegraSocio);
            }
        }
        // ALIMENTA OBJETO DE REGRAS PRINCIPAL
        if (!listaRetornoRegra.isEmpty()) {
            responseGlobalvida.setRegras(listaRetornoRegra);
        }
    }

    /**
     * Metodo para acionar motor de cálculo
     */
    private Object request(final RequestGlobalvida requestGlobalvida) throws Exception {
        AtomicBoolean bypass = new AtomicBoolean(false);
        ResponseGlobalvida responseGlobalvida = new ResponseGlobalvida();

        if (requestGlobalvida != null) {
            // CHECANDO CRÍTICAS PARA APLICAÇÃO DE REGRAS
            regraaceitacao(requestGlobalvida, responseGlobalvida);
            // CHECANDO CRÍTICAS PARA APLICAÇÃO DE REGRAS
            CoberturaConverter.converterRequest(requestGlobalvida);

            MotorRequestGlobalvida bom = new MotorRequestGlobalvida();

            bom.setPeriodicidadePagamento(requestGlobalvida.getPeriodicidadePagamento());
            bom.setCnae(requestGlobalvida.getCnae());
            bom.setCarregamento(requestGlobalvida.getCarregamento());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            if (requestGlobalvida.getDataCotacao() != null) {
                bom.setDataCotacao(dateFormat.format(dateFormat.parse(requestGlobalvida.getDataCotacao())));
            } else {
                String dataAtual = dateFormat.format(new Date());
                bom.setDataCotacao(dataAtual);
            }
            if (requestGlobalvida.getDataCriacaoCotacao() != null) {
                bom.setDataCriacaoCotacao(dateFormat.format(dateFormat.parse(requestGlobalvida.getDataCriacaoCotacao())));
            } else {
                String dataAtual = dateFormat.format(new Date());
                bom.setDataCriacaoCotacao(dataAtual);
            }
            responseGlobalvida.setPeriodicidadePagamento(requestGlobalvida.getPeriodicidadePagamento());
            responseGlobalvida.setCnae(requestGlobalvida.getCnae());
            responseGlobalvida.setCarregamento(requestGlobalvida.getCarregamento());

            ResponsePremio responsePremio = new ResponsePremio();

            int periodicidade = requestGlobalvida.getPeriodicidadePagamento();
            ResponsePremioPeriodo responsePremioPeriodo = new ResponsePremioPeriodo();
            /* --------------------
             * CALCULO FUNCIONARIOS
             * RESPONSE FUNCIONÁRIOS SÓ SERÁ INSTANCIADO SE NÃO FOI ANTERIORMENTE EM REGRAS
             */
            ResponseFuncionario responseFuncionario = responseGlobalvida.getFuncionarios() == null ? new ResponseFuncionario() : responseGlobalvida.getFuncionarios();
            double premioCoberturaFuncionarioMensal = 0.0;
            double premioCoberturaFuncionarioPeriodo = 0.0;
            double premioAssistenciaFuncionarioMensal = 0.0;
            double premioAssistenciaFuncionarioPeriodo = 0.0;
            if (requestGlobalvida.getFuncionarios() != null) {
                bom.setNumeroVidas(requestGlobalvida.getFuncionarios().getNumeroVidas());
                /* -----------------------
                 * COBERTURAS FUNCIONARIOS
                 */
                List<ResponseCobertura> coberturasFuncionarios = new ArrayList<>();

                AtomicReference<Double> capitalBasica = new AtomicReference<>(0.0);
                requestGlobalvida.getFuncionarios().getCoberturas().stream()
                        .filter(c -> c.getCodigoCobertura().equals("M") || c.getCodigoCobertura().equals("MA"))
                        .findFirst()
                        .ifPresent(c -> capitalBasica.set(c.getCapitalCobertura()));
                AtomicReference<Integer> tipoCapitalBasica = new AtomicReference<>(0);
                requestGlobalvida.getFuncionarios().getCoberturas().stream()
                        .filter(c -> c.getCodigoCobertura().equals("M") || c.getCodigoCobertura().equals("MA"))
                        .findFirst()
                        .ifPresent(c -> tipoCapitalBasica.set(c.getTipoCapital()));

                requestGlobalvida.getFuncionarios().getCoberturas().forEach(cobertura -> {
                    coberturasFuncionarios.add(
                            // ACIONA MOTOR DE CÁLCULO
                            popularCoberturasAcionaMotor(cobertura, bom, responseGlobalvida, capitalBasica, tipoCapitalBasica));
                });
                responseFuncionario.setNumeroVidas(requestGlobalvida.getFuncionarios().getNumeroVidas());
                responseFuncionario.setCoberturas(coberturasFuncionarios);

                premioCoberturaFuncionarioMensal = coberturasFuncionarios
                        .stream()
                        .mapToDouble(c -> (c.getPremio() + c.getIof()))
                        .sum();
                premioCoberturaFuncionarioPeriodo = coberturasFuncionarios
                        .stream()
                        .mapToDouble(c -> ((c.getPremio() + c.getIof()) * periodicidade))
                        .sum();
                /* -------------------------
                 * ASSISTENCIAS FUNCIONARIOS
                 */
                List<ResponseAssistencia> assistenciasFuncionarios = new ArrayList<>();
                requestGlobalvida.getFuncionarios().getAssistencias().forEach(assistencia -> {
                    assistenciasFuncionarios.add(
                            // ACIONA MOTOR DE CÁLCULO
                            popularAssistencias(assistencia, bom));
                });
                responseFuncionario.setAssistencias(assistenciasFuncionarios);
                premioAssistenciaFuncionarioMensal = assistenciasFuncionarios
                        .stream()
                        .mapToDouble(a -> (getRound(a.getPremio() * 1.0038, 2)))
                        .sum();
                premioAssistenciaFuncionarioPeriodo = assistenciasFuncionarios
                        .stream()
                        .mapToDouble(a -> (
                                getRound( getRound(a.getPremio() * periodicidade, 2) * 1.0038, 2)
                        ))
                        .sum();

                responseGlobalvida.setFuncionarios(responseFuncionario);
                responsePremio.setPremioFuncionario(getRound(premioCoberturaFuncionarioMensal + premioAssistenciaFuncionarioMensal, 2));
            }
            /* -----------------
             * CALCULO SOCIOS
             * RESPONSE SÓ SERÁ INSTANCIADO SE NÃO FOI ANTERIORMENTE EM REGRAS
             */
            ResponseSocio responseSocio = responseGlobalvida.getSocios() == null ? new ResponseSocio() : responseGlobalvida.getSocios();
            double premioCoberturaSocioMensal = 0.0;
            double premioCoberturaSocioPeriodo = 0.0;
            double premioAssistenciaSocioMensal = 0.0;
            double premioAssistenciaSocioPeriodo = 0.0;
            if (requestGlobalvida.getSocios() != null) {
                bom.setNumeroVidas(requestGlobalvida.getSocios().getNumeroVidas());
                bom.setCapitalUniforme(requestGlobalvida.getSocios().getCapitalUniforme());
                /* -----------------
                 * COBERTURAS SOCIOS
                 */
                List<ResponseCobertura> coberturasSocios = new ArrayList<>();

                AtomicReference<Double> capitalBasica = new AtomicReference<>(0.0);
                requestGlobalvida.getSocios().getCoberturas().stream()
                        .filter(c -> c.getCodigoCobertura().equals("M") || c.getCodigoCobertura().equals("MA"))
                        .findFirst()
                        .ifPresent(c -> capitalBasica.set(c.getCapitalCobertura()));
                AtomicReference<Integer> tipoCapitalBasica = new AtomicReference<>(0);
                requestGlobalvida.getSocios().getCoberturas().stream()
                        .filter(c -> c.getCodigoCobertura().equals("M") || c.getCodigoCobertura().equals("MA"))
                        .findFirst()
                        .ifPresent(c -> tipoCapitalBasica.set(c.getTipoCapital()));

                requestGlobalvida.getSocios().getCoberturas().forEach(cobertura -> {
                    coberturasSocios.add(
                            // ACIONA MOTOR DE CÁLCULO
                            popularCoberturasAcionaMotor(cobertura, bom, responseGlobalvida, capitalBasica, tipoCapitalBasica));
                });
                responseSocio.setNumeroVidas(requestGlobalvida.getSocios().getNumeroVidas());
                responseSocio.setCapitalUniforme(requestGlobalvida.getSocios().getCapitalUniforme());
                responseSocio.setCoberturas(coberturasSocios);
                premioCoberturaSocioMensal = coberturasSocios
                        .stream()
                        .mapToDouble(c -> (c.getPremio() + c.getIof()))
                        .sum();
                premioCoberturaSocioPeriodo = coberturasSocios
                        .stream()
                        .mapToDouble(c -> ((c.getPremio() + c.getIof()) * periodicidade))
                        .sum();

                /* -------------------
                 * ASSISTENCIAS SOCIOS
                 */
                List<ResponseAssistencia> assistenciasSocios = new ArrayList<>();
                requestGlobalvida.getSocios().getAssistencias().forEach(assistencia -> {
                    assistenciasSocios.add(
                            // ACIONA MOTOR DE CÁLCULO
                            popularAssistencias(assistencia, bom));
                });
                responseSocio.setAssistencias(assistenciasSocios);
                premioAssistenciaSocioMensal = assistenciasSocios
                        .stream()
                        .mapToDouble(a -> (getRound(a.getPremio() * 1.0038, 2)))
                        .sum();
                premioAssistenciaSocioPeriodo = assistenciasSocios
                        .stream()
                        .mapToDouble(a -> (
                                getRound( getRound(a.getPremio() * periodicidade, 2) * 1.0038, 2)
                        ))
                        .sum();
                responseGlobalvida.setSocios(responseSocio);
                responsePremio.setPremioSocio(getRound(premioCoberturaSocioMensal + premioAssistenciaSocioMensal, 2));
            }
            /* ------------
             * PREMIO FINAL
             */
            responsePremio.setPremioTotal(
                    getRound(
                            premioCoberturaFuncionarioMensal +
                                    premioAssistenciaFuncionarioMensal +
                                    premioCoberturaSocioMensal +
                                    premioAssistenciaSocioMensal, 2));
            responseGlobalvida.setPremioFinal(responsePremio);

            switch (periodicidade) {
                case 1 -> responsePremioPeriodo.setPeriodo("Mensal");
                case 2 -> responsePremioPeriodo.setPeriodo("Bimestral");
                case 3 -> responsePremioPeriodo.setPeriodo("Trimestral");
                case 4 -> responsePremioPeriodo.setPeriodo("Quadrimestral");
                case 6 -> responsePremioPeriodo.setPeriodo("Semestral");
                case 12 -> responsePremioPeriodo.setPeriodo("Anual");
                default -> throw new IllegalArgumentException("Periodicidade inválida: " + periodicidade);
            }
            responsePremioPeriodo.setPremioFuncionario(getRound(premioCoberturaFuncionarioPeriodo + premioAssistenciaFuncionarioPeriodo, 2));
            responsePremioPeriodo.setPremioSocio(getRound(premioCoberturaSocioPeriodo + premioAssistenciaSocioPeriodo, 2));
            responsePremioPeriodo.setPremioTotal(
                    getRound(premioCoberturaFuncionarioPeriodo + premioAssistenciaFuncionarioPeriodo + premioCoberturaSocioPeriodo + premioAssistenciaSocioPeriodo, 2)
            );
            responseGlobalvida.setPremioPeriodo(responsePremioPeriodo);
        }

        CoberturaConverter.converterResponse(responseGlobalvida);

        Object retornarIsto = new Object();
        if (!bypass.get())
            retornarIsto = responseGlobalvida;

        return retornarIsto;
    }

    private ResponseAssistencia popularAssistencias(
            Assistencia assistencia,
            MotorRequestGlobalvida bom) {

        AtomicReference<Exception> exp = new AtomicReference<>();
        ResponseAssistencia retorno = new ResponseAssistencia();
        MotorRequestGlobalvida assistenciaRequest = bom.getCopy();
        String msgCoverErr = msgErrMotor;
        assistenciaRequest.setCodigoAssistencia(assistencia.getCodigoAssistencia());
        retorno.setCodigoAssistencia(assistencia.getCodigoAssistencia());
        RequestRegra<MotorRequestGlobalvida> motor = new RequestRegra<>();
        motor.setFiltrar(RequestTipo.TUDO);
        motor.setVariaveis(assistenciaRequest);
        Retorno retornoCalculo = null;
        try {
            retornoCalculo = callEngine.run(url_calculoassistencia, motor);
        } catch (Throwable e2) {
            msgCoverErr = ";RequestGlobalvida URL Motor: " + url_calculoassistencia;
            log.error(msgCoverErr, e2);
            exp.set(new Exception(msgCoverErr, e2));
        }

        if (exp.get() == null && retornoCalculo != null && retornoCalculo.getCalculos() != null) {
            retornoCalculo.getCalculos().stream()
                    .findFirst().ifPresent(calculo -> {
                        calculo.getFormulas().stream()
                                .filter(formula -> "formulaPremioAssistencia".equals(formula.getFormula()))
                                .findFirst()
                                .ifPresent(formula -> {
                                    Double premioValue = parseDoubleValue(formula.getResultado());
                                    retorno.setPremio(new BigDecimal(premioValue).setScale(2, RoundingMode.HALF_UP).doubleValue());
                                });
                        calculo.getFormulas().stream()
                                .filter(formula -> "formulaPremioAssistenciaIof".equals(formula.getFormula()))
                                .findFirst()
                                .ifPresent(formula -> {
                                    Double iofValue = parseDoubleValue(formula.getResultado());
                                    retorno.setIof(new BigDecimal(iofValue).setScale(2, RoundingMode.HALF_UP).doubleValue());
                                });
                        calculo.getFormulas().stream()
                                .filter(formula -> "formulaPremioPuroAssistencia".equals(formula.getFormula()))
                                .findFirst()
                                .ifPresent(formula -> {
                                    Double premioPuroValue = parseDoubleValue(formula.getResultado());
                                    retorno.setPremioPuro(premioPuroValue);
                                });
                    });
        } else if (exp.get() == null) {
            msgCoverErr = ";RequestGlobalvida URL Motor: " + url_calculoassistencia;
            log.error(msgCoverErr);
        }

        return retorno;
    }

    private ResponseCobertura popularCoberturasAcionaMotor(
            Cobertura cobertura,
            MotorRequestGlobalvida bom,
            ResponseGlobalvida responseGlobal,
            AtomicReference<Double> capitalBasica,
            AtomicReference<Integer> tipoCapitalBasica) {

        AtomicReference<Exception> exp = new AtomicReference<>();
        ResponseCobertura retorno = new ResponseCobertura();
        MotorRequestGlobalvida coberturaRequest = bom.getCopy();
        String msgCoverErr = msgErrMotor;
        /*
         * APENAS PARA NAO GERAR ERRO NA TABELA DE DECISAO
         */
        coberturaRequest.setCodigoCobertura(cobertura.getCodigoCobertura());
        coberturaRequest.setCapitalCobertura(cobertura.getCapitalCobertura());
        coberturaRequest.setTipoCapital(cobertura.getTipoCapital());
        coberturaRequest.setTipoValor(cobertura.getTipoValor());
        coberturaRequest.setCapitalBasica(capitalBasica.get());
        coberturaRequest.setTipoCapitalBasica(tipoCapitalBasica.get());
        retorno.setCodigoCobertura(cobertura.getCodigoCobertura());
        RequestRegra<MotorRequestGlobalvida> motor = new RequestRegra<>();
        motor.setFiltrar(RequestTipo.TUDO);
        motor.setVariaveis(coberturaRequest);
        Retorno retornoCalculo = null;
        try {

            retornoCalculo = callEngine.run(url_calculocotacao, motor);

        } catch (Throwable e2) {
            msgCoverErr = ";RequestGlobalvida URL Motor: " + url_calculocotacao;
            log.error(msgCoverErr, e2);
            exp.set(new Exception(msgCoverErr, e2));
        }

        if (exp.get() == null) {
            List<RetornoRegra> listaRetornoRegraCobertura = new ArrayList<>();

            if (null != retornoCalculo.getRegras() && !retornoCalculo.getRegras().isEmpty()) {
                retornoCalculo.getRegras().forEach(regra -> {
                    RetornoRegra responseRegra = new RetornoRegra();
                    responseRegra.setCodigoRetorno(regra.getCodigoRetorno());
                    responseRegra.setNomeAmigavel(regra.getNomeAmigavel());
                    responseRegra.setDevolutiva(regra.getDevolutiva());
                    listaRetornoRegraCobertura.add(responseRegra);
                });
            }
            if (!listaRetornoRegraCobertura.isEmpty()) {
                retorno.setRegras(listaRetornoRegraCobertura);
            }
            if (retornoCalculo.getCalculos() != null && !retornoCalculo.getCalculos().isEmpty()) {
                retornoCalculo.getCalculos().stream()
                        .flatMap(calculo -> calculo.getFormulas().stream())
                        .forEach(formula -> {
                            if ("formulaCapital".equals(formula.getFormula())) {
                                BigDecimal capitalValueFinal = new BigDecimal(parseDoubleValue(formula.getResultado())).setScale(2, RoundingMode.HALF_UP);
                                retorno.setCapitalCobertura(capitalValueFinal);
                            }
                            if ("formulaPremioCobertura".equals(formula.getFormula())) { // Ajuste para Prêmio
                                Double premioValue = parseDoubleValue(formula.getResultado());
                                retorno.setPremio(new BigDecimal(premioValue).setScale(2, RoundingMode.HALF_UP).doubleValue());
                            }
                            if ("formulaPremioCoberturaIof".equals(formula.getFormula())) { // Ajuste para IOF
                                Double iofValue = parseDoubleValue(formula.getResultado());
                                retorno.setIof(new BigDecimal(iofValue).setScale(2, RoundingMode.HALF_UP).doubleValue());
                            }
                            if ("taxaPuraAnual".equals(formula.getFormula())) {  // Ajuste para TaxaPura
                                Double taxaPuraValue = parseDoubleValue(formula.getResultado());
                                retorno.setTaxaPura(new BigDecimal(taxaPuraValue).setScale(8, RoundingMode.HALF_UP).doubleValue());
                            }
                        });
            } else {
                msgCoverErr = ";RequestGlobalvida URL Motor: " + url_calculocotacao;
                log.error(msgCoverErr);
            }
        }

        return retorno;
    }

    private double getRound(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
