package com.graphaware.neo4j.discourse.config;

import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Neo4jConfiguration {

    @Value("${neo4j.host:localhost}")
    private String host;

    @Value("${neo4j.port:7687}")
    private String port;

    @Value("${neo4j.user:neo4j}")
    private String user;

    @Value("${neo4j.password:}")
    private String password;

    @Bean
    public Driver buildDriver() {
        AuthToken token = !password.equals("")
                ? AuthTokens.basic(user, password)
                : AuthTokens.none();

        return GraphDatabase.driver(String.format("bolt://%s:%d", host, Integer.valueOf(port)), token);
    }

}
