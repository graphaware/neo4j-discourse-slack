package com.graphaware.neo4j.discourse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphaware.neo4j.discourse.domain.Feed;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

public class FeedMapperTest {

    @Test
    public void testMappingJsonFeed() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String url = "https://community.neo4j.com/t/load-csv-status-when-using-periodic-commit/3372.json";

        Feed feed = mapper.readValue(new URL(url), Feed.class);
        assertEquals(2, feed.getPostStream().getPosts().size());
    }
}
