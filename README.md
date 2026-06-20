# Seguro Globalvida

Gateway do motor de regra e cálculo do Seguro Global Vida.
Tratamento de **request** e **response** para Sistemas chamadores.

## Request

```
{
    "periodicidadePagamento": 1,
    "cnae": "03.21-3-01",
    "carregamento": "258-A C",
    "numeroVidas": 10,
    "capitalUniforme": 300000,
    "coberturas": [
        {
            "codigoCobertura": "IEA"
        },
        {
            "codigoCobertura": "CBM"
        },
        {
            "capitalCobertura": 2000,
            "planoGF": 1,
            "codigoCobertura": "GF"
        }
    ],
    "assistencias": [
        {
            "codigoAssistencia": 1,
            "capitalAssistencia": 1500
        },
        {
            "codigoAssistencia": 2
        },
        {
            "codigoAssistencia": 3
        },
        {
            "codigoAssistencia": 4
        },
        {
            "codigoAssistencia": 5,
            "capitalAssistencia": 1500
        },
        {
            "codigoAssistencia": 6,
            "capitalAssistencia": 1500
        },
        {
            "codigoAssistencia": 7,
            "capitalAssistencia": 1500
        },
        {
            "codigoAssistencia": 8
        },
        {
            "codigoAssistencia": 9
        },
        {
            "codigoAssistencia": 10
        },
        {
            "codigoAssistencia": 11
        },
        {
            "codigoAssistencia": 12,
            "capitalAssistencia": 1500
        },
        {
            "codigoAssistencia": 13,
            "capitalAssistencia": 1500
        },
        {
            "codigoAssistencia": 14
        }
    ]
}
```

## Response

```
{
    "periodicidadePagamento": 1,
    "numeroVidas": 10,
    "cnae": "03.21-3-01",
    "carregamento": "258-A C",
    "capitalUniforme": 300000.0,
    "coberturas": [
        {
            "codigoCobertura": "CBM",
            "premio": 64.59
        },
        {
            "codigoCobertura": "IEA",
            "premio": 18.52
        },
        {
            "codigoCobertura": "GF",
            "capitalCobertura": 2000.0,
            "planoGF": 1,
            "premio": 9.61
        }
    ],
    "assistencias": [
        {
            "codigoAssistencia": 9,
            "nomeAssistencia": "2ª Opnião Médica",
            "valorAssistenciaMensal": 181.29
        },
        {
            "codigoAssistencia": 5,
            "nomeAssistencia": "Auxilio Funeral",
            "valorAssistenciaMensal": 3.23,
            "capitalAssistencia": 1500.0
        },
        {
            "codigoAssistencia": 13,
            "nomeAssistencia": "DMHO",
            "valorAssistenciaMensal": 30.43,
            "capitalAssistencia": 1500.0
        },
        {
            "codigoAssistencia": 2,
            "nomeAssistencia": "Assist. Corporate",
            "valorAssistenciaMensal": 128.44
        },
        {
            "codigoAssistencia": 14,
            "nomeAssistencia": "Assistência Odontológica Emergencial",
            "valorAssistenciaMensal": 16.63
        },
        {
            "codigoAssistencia": 4,
            "nomeAssistencia": "Cesta Basica Alimentar",
            "valorAssistenciaMensal": 8.69
        },
        {
            "codigoAssistencia": 3,
            "nomeAssistencia": "Cesta Natalina",
            "valorAssistenciaMensal": 19.03
        },
        {
            "codigoAssistencia": 7,
            "nomeAssistencia": "Rescição Contratual",
            "valorAssistenciaMensal": 3.23,
            "capitalAssistencia": 1500.0
        },
        {
            "codigoAssistencia": 1,
            "nomeAssistencia": "Morte Complementar",
            "valorAssistenciaMensal": 3.23,
            "capitalAssistencia": 1500.0
        },
        {
            "codigoAssistencia": 10,
            "nomeAssistencia": "Pet",
            "valorAssistenciaMensal": 62.83
        },
        {
            "codigoAssistencia": 6,
            "nomeAssistencia": "Doenças Congênitas",
            "valorAssistenciaMensal": 20.87,
            "capitalAssistencia": 1500.0
        },
        {
            "codigoAssistencia": 12,
            "nomeAssistencia": "DMH",
            "valorAssistenciaMensal": 30.43,
            "capitalAssistencia": 1500.0
        },
        {
            "codigoAssistencia": 11,
            "nomeAssistencia": "Orientação Financeira",
            "valorAssistenciaMensal": 2.4
        },
        {
            "codigoAssistencia": 8,
            "nomeAssistencia": "Assessoria profisional",
            "valorAssistenciaMensal": 18.11
        }
    ]
}
```
