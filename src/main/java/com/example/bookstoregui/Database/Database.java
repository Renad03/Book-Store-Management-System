package com.example.bookstoregui.Database;
import static com.mongodb.client.model.Filters.eq;

import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Database {
    private static Database db;
    private final MongoClient mongoClient;
    private final MongoDatabase database;


    private Database(String connectionString, String dbName) {
        System.out.println(connectionString);
        // Create a new MongoClient instance
        mongoClient = MongoClients.create(connectionString);
        // Get the database
        database = mongoClient.getDatabase(dbName);

    }

    public static Database getInstance() {
        Dotenv dotenv = Dotenv.load();
        String dbConnectionString = dotenv.get("DB_CONNECTION_STRING");
        String dbName = dotenv.get("DB_NAME");
        if (db == null) {
            System.out.println("Creating database aho");
            db = new Database(dbConnectionString, dbName);
        }
        return db;
    }
    public MongoCollection<Document> getCollection(String collectionName) {
        // Get the collection from the database
        return database.getCollection(collectionName);
    }

    public void close() {
        // Close the MongoClient instance
        if (mongoClient != null) {
            mongoClient.close();
        }
    }


}