package com.graphaware.neo4j.discourse.rss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphaware.neo4j.discourse.domain.Feed;
import com.graphaware.neo4j.discourse.domain.ForumPost;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RssItemProcessor implements Processor {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void process(Exchange exchange) {
        System.out.println("starting processing...");
        SyndFeedImpl feed = exchange.getIn().getBody(SyndFeedImpl.class);
        SyndEntry syndEntry = (SyndEntry) feed.getEntries().get(0);
        ForumPost forumPost = transform(syndEntry);
        exchange.getOut().setBody(forumPost);

    }

    private ForumPost transform(SyndEntry syndEntry) {
        String url = syndEntry.getLink();
        ForumPost forumPost = new ForumPost(url, syndEntry.getTitle(), getDescriptionText(syndEntry), syndEntry.getAuthor(), mapCategories(syndEntry), getTags(url), syndEntry.getPublishedDate());
        forumPost.setFeed(getFeed(url));

        return forumPost;
    }

    private List<String> mapCategories(SyndEntry syndEntry) {
        return (List<String>) syndEntry.getCategories().stream()
                .map(c -> {
                    return ((SyndCategoryImpl) c).getName();
                }).collect(Collectors.toList());
    }

    private String getDescriptionText(SyndEntry syndEntry) {
        return Jsoup.parse(syndEntry.getDescription().getValue()).select("blockquote").first().text();
    }

    private List<String> getTags(String url) {
        try {
            Feed feed = MAPPER.readValue(new URL(url + ".json"), Feed.class);

            return feed.getTags();
        } catch (Exception e) {
            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    private Feed getFeed(String url) {
        try {
            return MAPPER.readValue(new URL(url + ".json"), Feed.class);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


}
