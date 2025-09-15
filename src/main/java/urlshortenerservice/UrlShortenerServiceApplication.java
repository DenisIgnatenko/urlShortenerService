package urlshortenerservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import urlshortenerservice.config.redis.RedisConfig;

@SpringBootApplication
@EnableFeignClients("urlshortenerservice.client")
@OpenAPIDefinition(
        info = @Info(
                title = "URL Shortener Service",
                version = "1.0.0")
)
@EnableAsync
@EnableScheduling
@EnableRetry
public class UrlShortenerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerServiceApplication.class, args);
    }
}