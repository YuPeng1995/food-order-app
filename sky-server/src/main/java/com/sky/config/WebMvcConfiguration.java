package com.sky.config;

import com.sky.interceptor.JwtTokenInterceptor;
import com.sky.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@EnableSwagger2
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("Registering custom interceptors");

        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/admin/**", "/user/**")
                .excludePathPatterns("/admin/employee/login", "/user/user/register", "/user/user/login", "/user/user/confirm_email");

    }

    /**
     * 通过Swagger生成接口文档
     *
     * @return
     */
    @Bean
    public Docket docket1() {
        log.info("Generating API documentation");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("food-order-app")
                .version("2.0")
                .description("food-order-app API documentation")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Admin Apis")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.admin"))
                .paths(PathSelectors.any())
                .build()// 添加全局认证配置
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    @Bean
    public Docket docket2() {
        log.info("Generating API documentation");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("food-order-app")
                .version("2.0")
                .description("food-order-app API documentation")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("User Apis")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.user"))
                .paths(PathSelectors.any())
                .build()// 添加全局认证配置
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    private List<SecurityScheme> securitySchemes() {
        return Collections.singletonList(
                new ApiKey("token", "token", "header")
        );
    }

    private List<SecurityContext> securityContexts() {
        return Arrays.asList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.ant("/admin/**"))
                        .build(),
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.ant("/user/**"))
                        .build()
        );
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(
                new SecurityReference("token", authorizationScopes)
        );

    }

    /**
     * 设置静态资源映射
     *
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Setting static resource mapping...");
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
        registry.addResourceHandler("/v2/api-docs")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    /**
     * Global CORS configuration.
     */
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        log.info("Setting static cors mappings...");
        registry.addMapping("/**")  // Apply to all paths
                .allowedOrigins("http://localhost:5173")  // Allowed origin(s)
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allowed HTTP methods
                .allowedHeaders("*")  // Allowed headers
                .allowCredentials(true)  // Allow cookies
                .maxAge(3600);  // Pre-flight request cache time (1 hour)
    }

    // Convert time to yyyy-mm-dd
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("Setting message converters...");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0, converter);
    }
}
