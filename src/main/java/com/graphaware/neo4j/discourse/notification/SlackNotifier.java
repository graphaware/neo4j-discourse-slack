package com.graphaware.neo4j.discourse.notification;

import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackAttachment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SlackNotifier {

    @Value("${slack.webhook.url}")
    private String webhookUrl;

    @Value("${slack.webhook.displayName}")
    private String displayName;

    public void notifyNewPost(String author, String title, String url, String text, List<String> categories) throws Exception {
        new Slack(webhookUrl)
                .displayName(displayName)
                .push(buildSlackMessage(url, title, author, text, categories));
    }

    private SlackAttachment buildSlackMessage(String url, String title, String author, String text, List<String> categories) {
        return new SlackAttachment("New post on Neo4j Community")
                .author(author)
                .title(title, url)
                .text(text.substring(0, Math.min(255, text.length())) + " ...")
                .footer(StringUtils.join(categories, ", "));
    }


}
