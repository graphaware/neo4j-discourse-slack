package com.graphaware.neo4j.discourse.config;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Neo4jSchema {

    private final Driver driver;

    @Autowired
    public Neo4jSchema(Driver driver) {
        this.driver = driver;
    }

    public void assertSchema() {
        try (Session session = driver.session()) {
            session.run("CREATE CONSTRAINT ON (n:ForumPost) ASSERT n.id IS UNIQUE").consume();
            session.run("CREATE CONSTRAINT ON (n:User) ASSERT n.id IS UNIQUE").consume();
            session.run("CREATE CONSTRAINT ON (n:Tag) ASSERT n.name IS UNIQUE").consume();
            session.run("CREATE CONSTRAINT ON (n:Category) ASSERT n.name IS UNIQUE").consume();
        }
    }
}
