package com.graphaware.neo4j.discourse.persistence;

import com.graphaware.neo4j.discourse.domain.ForumPost;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class Neo4jPersister implements Processor {

    private final Driver driver;

    public Neo4jPersister(Driver driver) {
        this.driver = driver;
    }

    @Override
    public void process(Exchange exchange) {
        persist(exchange.getIn().getBody(ForumPost.class));
    }

    private void persist(ForumPost forumPost) {
        createPost(forumPost);
        createTags(forumPost);
        createCategories(forumPost);
    }

    private void createPost(ForumPost forumPost) {
        String query = "MERGE (post:ForumPost {id: $props.id}) SET post = $props " +
                "MERGE (n:User {id: $props.authorId}) " +
                "MERGE (n)-[:CREATED]->(post) ";
        try (Session session = driver.session()) {
            session.run(query, buildParams(forumPost)).consume();
        }
    }

    private void createTags(ForumPost forumPost) {
        String query = "MATCH (n:ForumPost {id: $props.id}) " +
                "UNWIND $props.tags AS tag " +
                "MERGE (t:Tag {name: tag }) " +
                "MERGE (t)-[:TAGS]->(n)";
        try (Session session = driver.session()) {
            session.run(query, buildParams(forumPost)).consume();
        }
    }

    private void createCategories(ForumPost forumPost) {
        String query =  "MATCH (n:ForumPost {id: $props.id}) " +
                "UNWIND $props.categories AS category " +
                "MERGE (c:Category {name: category} ) " +
                "MERGE (n)-[:IN_CATEGORY]->(c) ";

        try (Session session = driver.session()) {
            session.run(query, buildParams(forumPost)).consume();
        }
    }

    private Map<String, Object> buildParams(ForumPost forumPost) {
        return Collections.singletonMap("props", map(forumPost));
    }

    private Map<String, Object> map(ForumPost forumPost) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", forumPost.getUrl());
        m.put("author", forumPost.getAuthor());
        m.put("authorId", forumPost.getAuthorId());
        m.put("title", forumPost.getTitle());
        m.put("description", forumPost.getDescription());
        m.put("categories", forumPost.getCategories());
        m.put("tags", forumPost.getTags());
        m.put("date", forumPost.getDate());
        m.put("timestamp", forumPost.getTimestamp());

        return m;
    }
}
