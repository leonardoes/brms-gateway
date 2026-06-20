package gateway.web.config;

import contratos.globalvida.MotorRequestGlobalvida;
import contratos.RequestRegra;
import drools.Retorno;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CallEngine {

    private final WebClient webClient;

    public CallEngine(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .codecs(this::configurarLimiteDeMemoria)
                .build();
    }

    private void configurarLimiteDeMemoria(ClientCodecConfigurer clientCodecConfigurer) {
        clientCodecConfigurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024); // Configurando 10MB de limite
    }

    public Retorno run(String url, RequestRegra<MotorRequestGlobalvida> obj) {
        return webClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(obj), RequestRegra.class)
                .retrieve()
                .bodyToMono(Retorno.class)
                .block();
    }
    public Mono<Retorno> runAsync(String url, RequestRegra<MotorRequestGlobalvida> obj) {
        return webClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(obj), RequestRegra.class)
                .retrieve()
                .bodyToMono(Retorno.class);
    }

    /**
     * Chamada para motores que retornam tipo complexo (retorno.procedimento).
     * O corpo é repassado verbatim, sem mapear para o envelope {@link Retorno}.
     */
    public Object runProcedimento(String url, RequestRegra<?> obj) {
        return webClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(obj), RequestRegra.class)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}
