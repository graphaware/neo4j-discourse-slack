package com.graphaware.neo4j.discourse.filter;

import com.graphaware.neo4j.discourse.domain.ForumPost;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AlreadyProcessedFilter {

    private final Driver driver;

    @Autowired
    public AlreadyProcessedFilter(Driver driver) {
        this.driver = driver;
    }

    public boolean filter(ForumPost forumPost) {
        String query = "MATCH (n:ForumPost) WHERE n.id = $id RETURN n";

        try (Session session = driver.session()) {
            return !session.run(query, Collections.singletonMap("id", forumPost.getUrl())).hasNext();
        }
    }
}
