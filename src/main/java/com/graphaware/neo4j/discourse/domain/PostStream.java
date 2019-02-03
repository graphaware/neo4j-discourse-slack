package com.graphaware.neo4j.discourse.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostStream {

    private List<Post> posts;

    public PostStream() {
    }

    public List<Post> getPosts() {
        return posts;
    }
}
