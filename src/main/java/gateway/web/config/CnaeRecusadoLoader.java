package gateway.web.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class CnaeRecusadoLoader {

    private static final Set<String> cnaesRecusados = new HashSet<>();

    @PostConstruct
    public void init() {
        try {
            loadCnaesRecusados();
            log.info("CNAEs recusados carregados com sucesso. Total: {}", cnaesRecusados.size());
        } catch (IOException e) {
            log.error("Erro ao carregar arquivo cnaesRecusados.csv", e);
            throw new RuntimeException("Falha ao inicializar CNAEs recusados", e);
        }
    }

    private void loadCnaesRecusados() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/cnaesRecusados.csv");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String cnae = line.trim();
                if (!cnae.isEmpty()) {
                    cnaesRecusados.add(cnae);
                }
            }
        }
    }

    public static boolean isCnaeRecusado(String cnae) {
        if (cnae == null) {
            return false;
        }
        return cnaesRecusados.contains(cnae.trim());
    }

    public static Set<String> getCnaesRecusados() {
        return new HashSet<>(cnaesRecusados);
    }
}
