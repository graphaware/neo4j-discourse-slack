package com.graphaware.neo4j.discourse;

import com.graphaware.neo4j.discourse.config.Neo4jSchema;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscourseApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(DiscourseApplication.class, args);
		ctx.getBean(Neo4jSchema.class).assertSchema();
	}
}
