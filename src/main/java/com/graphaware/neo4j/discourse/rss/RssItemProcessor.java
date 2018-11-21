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

        exchange.getOut().setBody(transform(syndEntry));

    }

    private ForumPost transform(SyndEntry syndEntry) {
        String url = syndEntry.getLink();
        return new ForumPost(url, syndEntry.getTitle(), getDescriptionText(syndEntry), syndEntry.getAuthor(), mapCategories(syndEntry), getTags(url), syndEntry.getPublishedDate());
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


}
