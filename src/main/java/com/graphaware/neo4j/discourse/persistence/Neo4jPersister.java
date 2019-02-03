package com.graphaware.neo4j.discourse.persistence;

import com.graphaware.neo4j.discourse.domain.ForumPost;
import com.graphaware.neo4j.discourse.domain.Post;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        handleReplies(forumPost);
    }

    private void createPost(ForumPost forumPost) {
        String query = "MERGE (post:ForumPost {uid: $props.uid}) SET post = $props " +
                "MERGE (n:User {id: $props.authorId}) " +
                "MERGE (n)-[:CREATED]->(post) ";
        try (Session session = driver.session()) {
            session.run(query, buildParams(forumPost)).consume();
        }
    }

    private void createTags(ForumPost forumPost) {
        String query = "MATCH (n:ForumPost {uid: $props.uid}) " +
                "UNWIND $props.tags AS tag " +
                "MERGE (t:Tag {name: tag }) " +
                "MERGE (t)-[:TAGS]->(n)";
        try (Session session = driver.session()) {
            session.run(query, buildParams(forumPost)).consume();
        }
    }

    private void createCategories(ForumPost forumPost) {
        String query =  "MATCH (n:ForumPost {uid: $props.uid}) " +
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
        m.put("id", forumPost.getFeed().getPostStream().getPosts().get(0).getId());
        m.put("topicId", forumPost.getFeed().getPostStream().getPosts().get(0).getTopicId());
        m.put("uid", m.get("topicId") + "__" + m.get("id"));
        m.put("author", forumPost.getAuthor());
        m.put("authorId", forumPost.getAuthorId());
        m.put("title", forumPost.getTitle());
        m.put("description", forumPost.getDescription());
        m.put("categories", forumPost.getCategories());
        m.put("tags", forumPost.getTags());
        m.put("date", forumPost.getDate());
        m.put("timestamp", forumPost.getTimestamp());
        m.put("postNumber", forumPost.getFeed().getPostStream().getPosts().get(0).getPostNumber());

        return m;
    }

    public void handleReplies(ForumPost forumPost) {
        if (forumPost.getFeed().getPostStream().getPosts().size() == 1) {
            return;
        }

        Post original = forumPost.getFeed().getPostStream().getPosts().get(0);
        List<Post> replies = forumPost.getFeed().getPostStream().getPosts().subList(1, forumPost.getFeed().getPostStream().getPosts().size());
        replies.forEach(reply -> {
            createReplyPost(reply, original);
        });
    }

    private void createReplyPost(Post post, Post original) {
        String query = "MERGE (n:ForumPost {uid: $props.uid}) SET n+= $props " +
                "MERGE (user:User {id: $authorId}) " +
                "WITH n, user " +
                "MATCH (original:ForumPost {uid: $replyToId}) " +
                "MERGE (user)-[:REPLIED]->(n) " +
                "MERGE (n)-[:REPLY_TO]->(original) ";
        Map<String, Object> params = new HashMap<>();
        params.put("replyToId", original.getTopicId() + "__" + original.getId());
        params.put("authorId", "@" + post.getUsername());
        params.put("props", mapReply(post));

        try (Session session = driver.session()) {
            session.run(query, params).consume();
        }
    }

    private Map<String, Object> mapReply(Post post) {
        Map<String, Object> map = new HashMap<>();
        map.put("description", post.getCooked());
        map.put("date", post.getUpdatedAt());
        map.put("uid", post.getTopicId() + "__" + post.getId());

        return map;
    }
}
