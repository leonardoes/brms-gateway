package gateway.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class GatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class, args);
    }

    @PostConstruct
    public void init() {
        boolean win = System.getProperty("os.name").toLowerCase().contains("win");
        if (!win) System.setProperty("java.security.egd", "file:/dev/./urandom");
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }
}
