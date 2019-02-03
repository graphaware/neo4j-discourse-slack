package com.graphaware.neo4j.discourse.filter;

import com.graphaware.neo4j.discourse.domain.ForumPost;
import org.apache.camel.Body;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ForumPostFilter {

    @Value("${forum.filter.category:*}")
    private String tagsFilter;

    @Value("${forum.filter.title:*}")
    private String titleFilter;

    @Value("${forum.filter.text:*}")
    private String bodyFilter;

    public boolean filter(@Body ForumPost forumPost) {
        return filterByTag(forumPost) && filterOnTitle(forumPost) && filterOnText(forumPost);
    }

    private boolean filterOnText(ForumPost forumPost) {
        return bodyFilter.equals("*") || Arrays.asList(bodyFilter.split(","))
                .stream().anyMatch(s -> forumPost.getDescription().toLowerCase().contains(s.toLowerCase()));
    }

    private boolean filterOnTitle(ForumPost forumPost) {
        return titleFilter.equals("*") || Arrays.asList(titleFilter.split(","))
                .stream().anyMatch(f -> forumPost.getTitle().toLowerCase().contains(f.toLowerCase()));
    }

    private boolean filterByTag(ForumPost forumPost) {

        return tagsFilter.equals("*") || Arrays.asList(tagsFilter.split(","))
                .stream().anyMatch(s ->
                    forumPost.getCategories().contains(s) || forumPost.getTags().contains(s)
                );
    }
}
