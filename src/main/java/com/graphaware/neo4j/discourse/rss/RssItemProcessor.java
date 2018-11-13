package com.graphaware.neo4j.discourse.rss;

import com.graphaware.neo4j.discourse.notification.SlackNotifier;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RssItemProcessor implements Processor {

    @Autowired
    private SlackNotifier slackNotifier;

    @Override
    public void process(Exchange exchange) {
        SyndFeedImpl feed = exchange.getIn().getBody(SyndFeedImpl.class);
        feed.getEntries().forEach(entry -> {
            SyndEntry syndEntry = (SyndEntry) entry;
            try {
                extractAndNotify(syndEntry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void extractAndNotify(SyndEntry syndEntry) throws Exception {
        List<String> categories = mapCategories(syndEntry);
        String author = syndEntry.getAuthor();
        String title = syndEntry.getTitle();
        String text = getDescriptionText(syndEntry);
        String url = syndEntry.getLink();

        slackNotifier.notifyNewPost(author, title, url, text, categories);
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


}
