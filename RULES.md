# RULES SPECIFICATION

This document describe new drools rules tha have been created

In order to implement the rules below, use the following POJO classes
- Cobertura from /contratos/globalvida/request
- Assistencia from /contratos/globalvida/request
- Funcionario from /contratos/globalvida/request
- Socio from /contratos/globalvida/request
- ResponseCobertura from /contratos/globalvida/reponse
- ResponseAssistencia from /contratos/globalvida/reponse
- ResponseFuncionario from /contratos/globalvida/reponse
- ResponseSocio from /contratos/globalvida/reponse

To return rules messages add here, like this example
- RetornoRegra from /drools/RetornoRegra

RetornoRegra responseRegra = new RetornoRegra();
responseRegra.setCodigoRetorno(regra.getCodigoRetorno());
responseRegra.setNomeAmigavel(regra.getNomeAmigavel());
responseRegra.setDevolutiva(regra.getDevolutiva());
listaRetornoRegraFuncionario.add(responseRegra);

### Coberturas Interdenpendecies RULES

| Código apenas para referência e entendimento das dependências | Código que virá no response | Regras de interdependências entre coberturas |
|---|---|---|
| M | 13281 | MA não contratada |
| IEA | 13293 | M contratada |
| IPA | 13297 | M ou MA contratada |
| IFPD | 13290 | M contratada |
| MCONJ | 13282 | M contratada |
| MF | 13288 | MCONJ contratada |
| MC | 18730 | M contratada |
| DC | 13302 | M contratada |
| AF | 14556 | M contratada + ["Garantia Funeral" não contratado] |
| RC | 18731 | M contratada |
| MA | 13296 | M não contratada |
| MACONJ | 13298 | MA contratada |
| MAF | 13299 | MACONJ contradada |
| MAC | 18732 | MA contratada |
| DMHO | 13300 | MA contratada |
| GFI2000 | 13284 | M ou MA contratada + AF não contratado |
| GFI3000 | 13286 | M ou MA contratada + AF não contratado |
| GFI5000 | 13292 | M ou MA contratada + AF não contratado |
| GFI7000 | 17738 | M ou MA contratada + AF não contratado |
| GFI10000 | 17739 | M ou MA contratada + AF não contratado |
| GFI15000 | 17740 | M ou MA contratada + AF não contratado |
| GFI20000 | 17741 | M ou MA contratada + AF não contratado |
| GFF2000 | 16772 | M ou MA contratada + AF não contratado |
| GFF3000 | 16773 | M ou MA contratada + AF não contratado |
| GFF5000 | 16788 | M ou MA contratada + AF não contratado |
| GFF7000 | 17742 | M ou MA contratada + AF não contratado |
| GFF10000 | 17743 | M ou MA contratada + AF não contratado |
| GFF15000 | 17744 | M ou MA contratada + AF não contratado |
| GFF20000 | 17745 | M ou MA contratada + AF não contratado |
| CB | 13348 | M ou MA contratada |
| Cesta Natalidade | 13304 | M ou MA contratada |
| Assessoria Profissional | 16841 | M ou MA contratada |
| 2ª Opinião Médica | 16683 | M ou MA contratada |
| Assistência Pet | 18442 | M ou MA contratada |
| Orientação Financeira | 16682 | M ou MA contratada |
| Corporate | 13305 | M ou MA contratada (somente para grupo sócios) |

### Max capital RULES

### Age limite RULES

### Life limite RULES


## Payload Example
Check for classes from package /contratos/globalvida/request
Those classes represents this json example

``` json
{
    "periodicidadePagamento": 4,
    "cnae": "03.21-3-99",
    "carregamento": "644",
    "dataCotacao": "18-03-2025",
    "dataCriacaoCotacao": "07-03-2025",
    "funcionarios": {
        "numeroVidas": 10,
        "coberturas": [
            {
                "codigoCobertura": "13296",
                "capitalCobertura": 300000.0,
                "tipoCapital": 2
            },
            {
                "codigoCobertura": "13299",
                "capitalCobertura": 100000.0,
                "tipoCapital": 2
            },
            {
                "codigoCobertura": "13300",
                "capitalCobertura": 100000.0,
                "tipoCapital": 2
            },
            {
                "codigoCobertura": "16775",
                "capitalCobertura": 100000.0,
                "tipoCapital": 2
            },
            {
                "codigoCobertura": "18730",
                "capitalCobertura": 100000.0,
                "tipoCapital": 2
            },
            {
                "codigoCobertura": "18731",
                "capitalCobertura": 100000.0,
                "tipoCapital": 2
            },
            {
                "codigoCobertura": "18732",
                "capitalCobertura": 100000.0,
                "tipoCapital": 2
            }
        ],
        "assistencias": [
            {
                "codigoAssistencia": "13305"
            },
            {
                "codigoAssistencia": "13304"
            },
            {
                "codigoAssistencia": "16841"
            },
            {
                "codigoAssistencia": "16683"
            },
            {
                "codigoAssistencia": "18442"
            },
            {
                "codigoAssistencia": "16682"
            }
        ]
    },
    "socios": {
        "numeroVidas": 10,
        "coberturas": [
            {
                "codigoCobertura": "13296",
                "capitalCobertura": 100000.0,
                "tipoCapital": 2
            },
            {
                "codigoCobertura": "13299",
                "capitalCobertura": 100000.0,
                "tipoCapital": 2
            },
            {
                "codigoCobertura": "13300",
                "capitalCobertura": 100000.0,
                "tipoCapital": 2
            },
            {
                "codigoCobertura": "16775",
                "capitalCobertura": 100000.0,
                "tipoCapital": 2
            }
        ],
        "assistencias": [
            {
                "codigoAssistencia": "13305"
            },
            {
                "codigoAssistencia": "13304"
            }
        ]
    }
}
```