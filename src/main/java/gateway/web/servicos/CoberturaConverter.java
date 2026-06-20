package gateway.web.servicos;

import contratos.globalvida.request.RequestGlobalvida;
import contratos.globalvida.request.Socio;
import contratos.globalvida.request.Funcionario;
import contratos.globalvida.request.Cobertura;
import contratos.globalvida.request.Assistencia;
import contratos.globalvida.response.ResponseGlobalvida;
import contratos.globalvida.response.ResponseSocio;
import contratos.globalvida.response.ResponseFuncionario;
import contratos.globalvida.response.ResponseCobertura;
import contratos.globalvida.response.ResponseAssistencia;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class CoberturaConverter {
    private static final Map<String, String> CODIGO_PARA_SIGLA = new HashMap<>();
    private static final Map<String, String> SIGLA_PARA_CODIGO = new HashMap<>();

    static {
        initializeMaps();
    }

    private static void initializeMaps() {
        adicionarMapeamento("13281", "M");
        adicionarMapeamento("13293", "IEA");
        //adicionarMapeamento("13289", "IPAVG100");
        adicionarMapeamento("13289", "IPAVG");
        adicionarMapeamento("13297", "IPAAP");
        adicionarMapeamento("13290", "IFPD");
        adicionarMapeamento("13282", "MCONJ");
        adicionarMapeamento("13288", "MF50");
        adicionarMapeamento("13291", "MC");
        adicionarMapeamento("13302", "DC");
        adicionarMapeamento("13295", "AF");
        adicionarMapeamento("13301", "RC");
        adicionarMapeamento("13296", "MA");
        adicionarMapeamento("13298", "MACONJ");
        adicionarMapeamento("13299", "MAF50");
        adicionarMapeamento("13303", "MAC");
        adicionarMapeamento("13300", "DMHO");
        adicionarMapeamento("13284", "GFI2000");
        adicionarMapeamento("13286", "GFI3000");
        adicionarMapeamento("16775", "GFI5000"); 
        adicionarMapeamento("13292", "GFI5000VG");
        adicionarMapeamento("17738", "GFI7000");
        adicionarMapeamento("17739", "GFI10000");
        adicionarMapeamento("17740", "GFI15000");
        adicionarMapeamento("17741", "GFI20000");
        adicionarMapeamento("16772", "GFF2000");
        adicionarMapeamento("16773", "GFF3000");
        adicionarMapeamento("16788", "GFF5000");
        adicionarMapeamento("17742", "GFF7000");
        adicionarMapeamento("17743", "GFF10000");
        adicionarMapeamento("17744", "GFF15000");
        adicionarMapeamento("17745", "GFF20000");
        adicionarMapeamento("13348", "CB4500");
        adicionarMapeamento("18730", "DDM");
        adicionarMapeamento("18731", "VR");
        adicionarMapeamento("18732", "DDMA");
        adicionarMapeamento("13305", "assistcorp");
        adicionarMapeamento("13304", "cestanat");
        adicionarMapeamento("16841", "assesprof");
        adicionarMapeamento("16683", "2opmed");
        adicionarMapeamento("18442", "pet");
        adicionarMapeamento("16682", "orientfin");
    }

    private static void adicionarMapeamento(String codigo, String sigla) {
        CODIGO_PARA_SIGLA.put(codigo, sigla);
        SIGLA_PARA_CODIGO.put(sigla, codigo);
    }

    /**
     * Converte os códigos do request para siglas
     */
    public static void converterRequest(RequestGlobalvida request) {
        if (request == null) return;

        if (request.getSocios() != null) {
            converterCodigosSocio(request.getSocios());
        }

        if (request.getFuncionarios() != null) {
            converterCodigosFuncionario(request.getFuncionarios());
        }
    }

    /**
     * Converte as siglas do response para códigos
     */
    public static void converterResponse(ResponseGlobalvida response) {
        if (response == null) return;

        if (response.getSocios() != null) {
            converterSiglasResponseSocio(response.getSocios());
        }

        if (response.getFuncionarios() != null) {
            converterSiglasResponseFuncionario(response.getFuncionarios());
        }
    }

    private static void converterCodigosSocio(Socio socio) {
        if (socio.getCoberturas() != null) {
            socio.getCoberturas().forEach(cobertura -> {
                if (cobertura.getCodigoCobertura() != null) {
                    cobertura.setCodigoCobertura(converterParaSigla(cobertura.getCodigoCobertura()));
                }
            });
        }

        if (socio.getAssistencias() != null) {
            socio.getAssistencias().forEach(assistencia -> {
                if (assistencia.getCodigoAssistencia() != null) {
                    assistencia.setCodigoAssistencia(converterParaSigla(assistencia.getCodigoAssistencia()));
                }
            });
        }
    }

    private static void converterCodigosFuncionario(Funcionario funcionario) {
        if (funcionario.getCoberturas() != null) {
            funcionario.getCoberturas().forEach(cobertura -> {
                if (cobertura.getCodigoCobertura() != null) {
                    cobertura.setCodigoCobertura(converterParaSigla(cobertura.getCodigoCobertura()));
                }
            });
        }

        if (funcionario.getAssistencias() != null) {
            funcionario.getAssistencias().forEach(assistencia -> {
                if (assistencia.getCodigoAssistencia() != null) {
                    assistencia.setCodigoAssistencia(converterParaSigla(assistencia.getCodigoAssistencia()));
                }
            });
        }
    }

    private static void converterSiglasResponseSocio(ResponseSocio socio) {
        if (socio.getCoberturas() != null) {
            socio.getCoberturas().forEach(cobertura -> {
                if (cobertura.getCodigoCobertura() != null) {
                    cobertura.setCodigoCobertura(converterParaCodigo(cobertura.getCodigoCobertura()));
                }
            });
        }

        if (socio.getAssistencias() != null) {
            socio.getAssistencias().forEach(assistencia -> {
                if (assistencia.getCodigoAssistencia() != null) {
                    assistencia.setCodigoAssistencia(converterParaCodigo(assistencia.getCodigoAssistencia()));
                }
            });
        }
    }

    private static void converterSiglasResponseFuncionario(ResponseFuncionario funcionario) {
        if (funcionario.getCoberturas() != null) {
            funcionario.getCoberturas().forEach(cobertura -> {
                if (cobertura.getCodigoCobertura() != null) {
                    cobertura.setCodigoCobertura(converterParaCodigo(cobertura.getCodigoCobertura()));
                }
            });
        }

        if (funcionario.getAssistencias() != null) {
            funcionario.getAssistencias().forEach(assistencia -> {
                if (assistencia.getCodigoAssistencia() != null) {
                    assistencia.setCodigoAssistencia(converterParaCodigo(assistencia.getCodigoAssistencia()));
                }
            });
        }
    }

    /**
     * Converte as siglas para códigos no response do cálculo reverso.
     *
     * O motor entrega tipo complexo (passthrough), com a estrutura:
     *   { ..., funcionarios: { numeroVidas, opcao1|opcao2|opcao3: { coberturas: [ { codigoCobertura, ... } ] } } }
     * Apenas o campo codigoCobertura de cada cobertura é remapeado (sigla -> código),
     * reaproveitando o mesmo dicionário SIGLA_PARA_CODIGO do fluxo forward.
     */
    @SuppressWarnings("unchecked")
    public static Object converterResponseReverso(Object motorResponse) {
        if (!(motorResponse instanceof Map)) return motorResponse;

        Object funcionariosObj = ((Map<String, Object>) motorResponse).get("funcionarios");
        if (!(funcionariosObj instanceof Map)) return motorResponse;

        ((Map<String, Object>) funcionariosObj).values().forEach(opcaoObj -> {
            if (!(opcaoObj instanceof Map)) return; // ignora numeroVidas e demais escalares
            Object coberturasObj = ((Map<String, Object>) opcaoObj).get("coberturas");
            if (!(coberturasObj instanceof List)) return;
            ((List<Object>) coberturasObj).forEach(coberturaObj -> {
                if (!(coberturaObj instanceof Map)) return;
                Map<String, Object> cobertura = (Map<String, Object>) coberturaObj;
                Object codigo = cobertura.get("codigoCobertura");
                if (codigo instanceof String) {
                    cobertura.put("codigoCobertura", converterParaCodigo((String) codigo));
                }
            });
        });

        return motorResponse;
    }

    private static String converterParaSigla(String codigo) {
        return CODIGO_PARA_SIGLA.getOrDefault(codigo, codigo);
    }

    private static String converterParaCodigo(String sigla) {
        return SIGLA_PARA_CODIGO.getOrDefault(sigla, sigla);
    }
}