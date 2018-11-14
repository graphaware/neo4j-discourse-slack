package com.graphaware.neo4j.discourse.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ForumPost {

    private final String url;

    private final String title;

    private final String description;

    private final String author;

    private final List<String> categories;

    private final List<String> tags;

    public ForumPost(String url, String title, String description, String author, List<String> categories, List<String> tags) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.author = author;
        this.categories = categories;
        this.tags = tags;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getTags() {
        return tags;
    }

    public String joinedTagsAndCategories() {
        List<String> c = categories;
        c.addAll(tags);

        return StringUtils.join(c, ", ");
    }
}
