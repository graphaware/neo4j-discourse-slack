## GraphAware Neo4j Discourse

Application that sends notifications about a new post on https://community.neo4j.com to a Slack Incoming Webhook.

### Configuration

* `slack.webhook.url` : The slack incoming webhook url
* `slack.webhook.displayName` : The name used for displaying messages in the channel
* `forum.filter.categories` : A comma separated list of categories or tags to filter on, eg : "Announcements,cypher" : default is *
* `forum.filter.title` : A comma separated list of terms where any of them is a selection criteria in the title
* `forum.filter.text` : A comma separated list of terms where any of them is a selection criteria in the post text

