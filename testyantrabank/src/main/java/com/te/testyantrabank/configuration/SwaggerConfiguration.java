package com.te.testyantrabank.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = { "com.te.testyantrabank" })
public class SwaggerConfiguration {
	@Autowired
	private SwaggerProperties swaggerProperties;
	@Bean
	public Docket bankApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(customMetaInfo())
				.securityContexts(Arrays.asList(securityContext())).securitySchemes(Arrays.asList(apiKey())).select()
				.apis(RequestHandlerSelectors.basePackage("com.te.testyantrabank")).paths(PathSelectors.any()).build();
	}

	private ApiInfo customMetaInfo() {
		return new ApiInfo(swaggerProperties.getTitle(),
				swaggerProperties.getDoc(),swaggerProperties.getVersion(), swaggerProperties.getTermsOfService(),
				new Contact(swaggerProperties.getUsername(), swaggerProperties.getWebsite(), swaggerProperties.getEmail()), swaggerProperties.getLicense(),
				swaggerProperties.getLicenseurl(), Collections.emptyList());
		}

	private ApiKey apiKey() {
		return new ApiKey("Bearer", "Authorization", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(securityReference()).build();
	}

	private List<SecurityReference> securityReference() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverthing");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("Bearer", authorizationScopes));
	}

}
