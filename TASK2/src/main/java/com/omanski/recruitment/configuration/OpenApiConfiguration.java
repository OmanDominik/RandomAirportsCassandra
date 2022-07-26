package com.omanski.recruitment.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

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
