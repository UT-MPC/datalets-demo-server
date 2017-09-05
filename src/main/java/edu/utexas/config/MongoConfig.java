package edu.utexas.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Collections;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

	private final Logger log = LoggerFactory.getLogger(MongoConfig.class);

	private String host = System.getenv("OPENSHIFT_MONGODB_DB_HOST");

	private Integer port = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));

	private String username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");

	private String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");

	private String database = "datalets";

	@Bean
	public ValidatingMongoEventListener validatingMongoEventListener() {
		return new ValidatingMongoEventListener(validator());
	}
	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}

	@Override
	public String getDatabaseName() {
		return database;
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		log.error("Username: " + username + ", Password: " + password);
		return new MongoClient(Collections.singletonList(new ServerAddress(host, port)),
				Collections.singletonList(MongoCredential.createCredential(username, database, password.toCharArray())));
	}
}
