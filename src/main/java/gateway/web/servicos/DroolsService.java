package gateway.web.servicos;

import contratos.globalvida.request.RequestGlobalvida;
import drools.RetornoRegra;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DroolsService {

    private final KieContainer kieContainer;

    public DroolsService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public void fireRulesFuncionarios(RequestGlobalvida cotacao, List<RetornoRegra> listaRetornoRegraFuncionario) {
        KieSession kieSession = kieContainer.newKieSession("funcionarios-rules");
        try {
            kieSession.setGlobal("listaRetornoRegraFuncionario", listaRetornoRegraFuncionario);
            kieSession.insert(cotacao);
            kieSession.fireAllRules();
        } finally {
            if (kieSession != null) {
                kieSession.dispose();
            }
        }
    }

    public void fireRulesSocios(RequestGlobalvida cotacao, List<RetornoRegra> listaRetornoRegraSocio) {
        KieSession kieSession = kieContainer.newKieSession("socios-rules");
        try {
            kieSession.setGlobal("listaRetornoRegraSocio", listaRetornoRegraSocio);
            kieSession.insert(cotacao);
            kieSession.fireAllRules();
        } finally {
            if (kieSession != null) {
                kieSession.dispose();
            }
        }
    }

    public void fireRulesVidasLimit(RequestGlobalvida cotacao, List<RetornoRegra> listaRetornoRegra) {
        KieSession kieSession = kieContainer.newKieSession("vidas-limit-rules");
        try {
            kieSession.setGlobal("listaRetornoRegra", listaRetornoRegra);
            kieSession.insert(cotacao);
            kieSession.fireAllRules();
        } finally {
            if (kieSession != null) {
                kieSession.dispose();
            }
        }
    }
}
