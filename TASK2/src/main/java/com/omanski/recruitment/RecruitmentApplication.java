package com.omanski.recruitment;

import com.omanski.recruitment.model.Airport;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication

public class RecruitmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecruitmentApplication.class, args);
    }

    @Bean
    public OpenApiCustomiser schemaCustomizer() {
        ResolvedSchema resolvedSchema = ModelConverters
                .getInstance()
                .resolveAsResolvedSchema(
                        new AnnotatedType(Airport.class).resolveAsRef(false)
                );

        return openApi -> openApi.schema(resolvedSchema.schema.getName(), resolvedSchema.schema);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
