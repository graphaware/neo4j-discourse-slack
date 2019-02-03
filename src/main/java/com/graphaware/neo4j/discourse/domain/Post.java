package com.graphaware.neo4j.discourse.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {

    private String id;

    private String name;

    private String username;

    private String avatarTemplate;

    private String cooked;

    private String updatedAt;

    @JsonProperty("post_number")
    private int postNumber;

    @JsonProperty("reply_to_post_number")
    private int replyTo;

    @JsonProperty("topic_id")
    private String topicId;

    public Post() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarTemplate() {
        return avatarTemplate;
    }

    public String getCooked() {
        return cooked;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getPostNumber() {
        return postNumber;
    }

    public String getTopicId() {
        return topicId;
    }

    public int getReplyTo() {
        return replyTo;
    }
}
