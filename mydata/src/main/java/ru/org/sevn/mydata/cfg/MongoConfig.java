package ru.org.sevn.mydata.cfg;

import org.springframework.context.annotation.Configuration;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//import com.mongodb.connection.DefaultClusterFactory;

@EnableMongoRepositories (basePackages = "ru.org.sevn.mydata.repo")
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Autowired
    private MongoProperties mongoProperties;

    @Override
    protected String getDatabaseName () {
        return mongoProperties.getDatabase ();
    }

    @Override
    public boolean autoIndexCreation () {
        return true;
    }

    @Override
    public MongoClient mongoClient () {
        System.out.println ("Connect to mongodb uri=" + mongoProperties.getUri () + " DatabaseName=" + mongoProperties.getDatabase ());

        final ConnectionString connectionString = new ConnectionString (mongoProperties.getUri ());
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder ()
                .applyConnectionString (connectionString)
                .build ();

        return MongoClients.create (mongoClientSettings);
    }

    //    @Bean
    //    MongoTransactionManager transactionManager (MongoDatabaseFactory dbFactory) {
    //        return new MongoTransactionManager (dbFactory);
    //    }

}
