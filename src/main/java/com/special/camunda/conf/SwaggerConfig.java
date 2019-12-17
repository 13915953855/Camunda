package com.special.camunda.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = {"com.special.camunda"})
public class SwaggerConfig {
    @Bean
    public Docket swaggerPluggin() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.globalOperationParameters(createGlobalParams())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.special.camunda"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("Camunda BPM与Spring Boot集成服务列表")
                        .description("©2019 Copyright. Powered By xusen.")
                        .contact(new Contact("xusen", "", "xusen@cmiot.chinamobile.com"))
                        .license("Camunda BPM 与 Spring Boot集成")
                        .build())
                .useDefaultResponseMessages(false);
    }
    
    private List<Parameter> createGlobalParams(){
        List<Parameter> aParameters = new ArrayList<Parameter>();
        
        //可以添加多个header或参数
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder
                .parameterType("header") //参数类型支持header, cookie, body, query etc
                .name("camundaToken") //参数名
                .defaultValue("camundaToken#000") //默认值
                .description("header中camundaToken字段")
                .modelRef(new ModelRef("string"))//指定参数值的类型
                .required(false).build(); //非必需，这里是全局配置，然而在登陆的时候是不用验证的

        aParameters.add(aParameterBuilder.build());
        return aParameters;
    }
}
