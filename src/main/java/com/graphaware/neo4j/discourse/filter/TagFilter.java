package com.graphaware.neo4j.discourse.filter;

import com.graphaware.neo4j.discourse.domain.ForumPost;
import org.apache.camel.Body;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TagFilter {

    @Value("${forum.filter.category:*}")
    private String filterString;

    public boolean filterByTag(@Body ForumPost body) {

        if (filterString.equals("*")) {
            return true;
        }

        List<String> filter = Arrays.asList(filterString.split(","));
        for (String s : filter) {
            if (body.getCategories().contains(s) || body.getTags().contains(s)) {
                System.out.println("tag " + s + " found.");
                return true;
            }
        }

        System.out.println("Filter could not match anything");
        return false;
    }

}
