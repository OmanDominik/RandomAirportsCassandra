package com.omanski.recruitment.configuration;

import com.omanski.recruitment.model.Airport;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Value;
import org.apache.tomcat.jni.Address;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
@OpenAPIDefinition(
        info = @Info(
                title = "Random airports data handler",
                description = "" +
                        "an application that provides an API that allows you to perform operations on randomly generated objects representing airports",
                contact = @Contact(
                        name = "Author's github",
                        url = "https://github.com/OmanDominik",
                        email = "dominoma013@gmail.com"
                )),
        servers = @Server(url = "http://localhost:9090")
)
public class OpenApiConfiguration {

}
