package com.omanski.recruitment.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Arrays;
import java.util.List;

@ConditionalOnProperty(value = "cassandraConfig", havingValue = "production")
@Configuration
@EnableCassandraRepositories(basePackages = "com.omanski.recruitment.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspace;

    @Override
    public String getKeyspaceName() {
        return keyspace;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification
                .createKeyspace(getKeyspaceName());
        return Arrays.asList(specification);
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"com.omanski.recruitment.model"};
    }
}
