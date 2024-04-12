package semicolon.MeetOn_Channel.domain.global.util;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(
        title = "MeetOn Channel API",
        description = "MeetOn : API 명세서",
        version = "v1.0.0"))
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(@Value("${openapi.service.url}") String url) {
        log.info("serverUrl={}", url);
        return new OpenAPI()
                .servers(List.of(new Server().url(url)))
                .components(new Components().addSecuritySchemes("Bearer",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer"))
                .info(new io.swagger.v3.oas.models.info.Info().title("KEA Project-SemiColon")
                        .description("Channel 관련 API")
                        .version("v0 0.1"));
    }
}
