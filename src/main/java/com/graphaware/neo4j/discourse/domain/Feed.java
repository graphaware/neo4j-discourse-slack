package com.graphaware.neo4j.discourse.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Feed {

    @JsonProperty("post_stream")
    private PostStream postStream;

    private List<String> tags;

    public Feed() {
    }

    public List<String> getTags() {
        return tags;
    }

    public PostStream getPostStream() {
        return postStream;
    }
}
