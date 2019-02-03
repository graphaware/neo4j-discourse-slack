package com.graphaware.neo4j.discourse.filter;

import com.graphaware.neo4j.discourse.domain.ForumPost;
import org.apache.camel.Body;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ExistingReplyFilter {

    private final Driver driver;

    @Autowired
    public ExistingReplyFilter(Driver driver) {
        this.driver = driver;
    }

    public boolean filter(@Body ForumPost forumPost) {
        int lastReplyId = forumPost.getFeed().getPostStream().getPosts().get(forumPost.getFeed().getPostStream().getPosts().size() - 1).getPostNumber();
        boolean exist;

        try (Session session = driver.session()) {
            exist = session.run("MATCH (n:ForumPost) WHERE n.id = $id RETURN n", Collections.singletonMap("id", lastReplyId)).hasNext();
        }

        return exist;
    }

}
