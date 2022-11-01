package com.programmers.configuration.swagger;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

/**
 * Swagger configuration for this application.
 */

@Configuration
@ConfigurationProperties(prefix = "swagger.app-info")
@Setter
public class SwaggerConfig {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String HEADER = "header";
    public static final String JWT = "JWT";

    // swagger properties
    private String title;
    private String description;
    private String version;
    private String terms;
    private String contactName;
    private String contactUrl;
    private String email;
    private String licenceText;
    private String licenceUrl;

    /**
     * Api information bean with all the values available in application properties with prefix as swagger.app-info
     *
     * @return the object to be used in the swagger docket.
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(title, description, version, terms,
                new Contact(contactName, contactUrl, email),
                licenceText, licenceUrl, Collections.emptyList());
    }

    /**
     * The docket bean for springfox swagger to show all the available endpoints.
     *
     * @return the docket object to be used in the swagger api documentation.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Authorization policy with the API Key
     *
     * @return the ApiKey object to be used for sending authorization header
     */
    private ApiKey apiKey() {
        return new ApiKey(JWT, AUTHORIZATION_HEADER, HEADER);
    }

    /**
     * A security context for authorization.
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    /**
     * Default authorization plan is se to global access with successful authorization.
     */
    List<SecurityReference> defaultAuth() {
        return Collections.singletonList(
                new SecurityReference(
                        JWT,
                        new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")}
                ));
    }

}