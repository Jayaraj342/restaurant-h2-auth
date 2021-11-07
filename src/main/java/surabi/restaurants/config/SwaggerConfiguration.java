package surabi.restaurants.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import surabi.restaurants.controller.SurabiRestaurantsController;

@Configuration
public class SwaggerConfiguration {

    //http://localhost:8080/swagger-ui/

    @Bean
    public Docket swaggerConfig() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage(SurabiRestaurantsController.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build();
    }
}
