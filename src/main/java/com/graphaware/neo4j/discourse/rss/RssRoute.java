package com.graphaware.neo4j.discourse.rss;

import com.graphaware.neo4j.discourse.filter.AlreadyProcessedFilter;
import com.graphaware.neo4j.discourse.filter.ForumPostFilter;
import com.graphaware.neo4j.discourse.notification.SlackNotifier;
import com.graphaware.neo4j.discourse.persistence.Neo4jPersister;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RssRoute extends RouteBuilder {

    @Autowired
    private RssItemProcessor rssItemProcessor;

    @Autowired
    private SlackNotifier slackNotifier;

    @Autowired
    private Neo4jPersister neo4jPersister;

    @Autowired
    private ForumPostFilter forumPostFilter;

    @Autowired
    private AlreadyProcessedFilter alreadyProcessedFilter;

    @Override
    public void configure() throws Exception {
        from("rss:https://community.neo4j.com/latest.rss")
                .process(rssItemProcessor)
                .filter().method(alreadyProcessedFilter, "filter")
                .filter().method(forumPostFilter, "filter")
                .process(neo4jPersister)
                .process(slackNotifier);
    }
}
