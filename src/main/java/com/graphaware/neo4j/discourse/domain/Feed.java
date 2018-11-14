package com.graphaware.neo4j.discourse.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Feed {

    List<String> tags;

    public Feed() {
    }

    public List<String> getTags() {
        return tags;
    }
}
