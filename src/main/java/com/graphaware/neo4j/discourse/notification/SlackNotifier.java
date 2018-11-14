package com.graphaware.neo4j.discourse.notification;

import com.graphaware.neo4j.discourse.domain.ForumPost;
import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackAttachment;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SlackNotifier implements Processor {

    @Value("${slack.webhook.url}")
    private String webhookUrl;

    @Value("${slack.webhook.displayName}")
    private String displayName;

    @Override
    public void process(Exchange exchange) throws Exception {
        ForumPost forumPost = exchange.getIn().getBody(ForumPost.class);

        notifyNewPost(forumPost);
    }

    public void notifyNewPost(ForumPost forumPost) throws Exception {
        new Slack(webhookUrl)
                .displayName(displayName)
                .push(buildSlackMessage(forumPost.getUrl(), forumPost.getTitle(), forumPost.getAuthor(), forumPost.getDescription(), forumPost.joinedTagsAndCategories()));
    }

    private SlackAttachment buildSlackMessage(String url, String title, String author, String text, String categories) {
        return new SlackAttachment("New post on Neo4j Community")
                .author(author)
                .title(title, url)
                .text(text.substring(0, Math.min(255, text.length())) + " ...")
                .footer(categories);
    }


}
