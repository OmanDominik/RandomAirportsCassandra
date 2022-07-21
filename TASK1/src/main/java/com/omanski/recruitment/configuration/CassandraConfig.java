package com.omanski.recruitment.configuration;

//@Configuration
//public class CassandraConfig extends AbstractCassandraConfiguration {
//
//    @Value("${spring.data.cassandra.keyspace-name}")
//    private String keyspace;
//
//    @Override
//    protected String getKeyspaceName() {
//        return keyspace;
//    }
//
//    @Override
//    protected KeyspacePopulator keyspacePopulator() {
//        ResourceKeyspacePopulator keyspacePopulate = new ResourceKeyspacePopulator();
//        keyspacePopulate.setSeparator(";");
//        keyspacePopulate.setScripts(new ClassPathResource("init.cql"));
//        return keyspacePopulate;
//    }
//
//
//
//}