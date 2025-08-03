package com.example.bookstoregui.Database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.example.bookstoregui.Model.Customer;
import com.example.bookstoregui.Model.User;

import java.util.ArrayList;
import java.util.List;

public class UserCRUD {
    private final MongoCollection<Document> collection;
    private final MongoCollection<Document> reviewCollection;
    CartCRUD cartCRUD = new CartCRUD();

    public UserCRUD() {
        Database database = Database.getInstance();
        this.collection = database.getCollection("user");
        this.reviewCollection = database.getCollection("review");
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (Document doc : collection.find()) {
            users.add(User.fromDocument(doc));
        }
        return users;
    }

    public void createUser(User user) {
        collection.insertOne(user.toDocument());
        createUserCart(user.getUsername());
    }
    public void createUserCart(String username){
        User user = getUserByUsername(username);
        cartCRUD.createCart(user.getId());
    }

    public void deleteUser(User user) {
        collection.deleteOne(user.toDocument());
    }

    public void updateUser(User filterUser, User updateUser) {
        collection.updateOne(
                filterUser.toDocument(),
                new Document("$set", updateUser.toDocument())
        );
    }

//    public User getUserByUsername(String userId) {
//        Document filter = new Document("username", userId);
//        return getUserByFilter(filter);
//    }

    public Customer getUserByUsername(String username) {
        Document doc = collection.find(Filters.eq("username", username)).first();

        // Check if the document exists
        if (doc == null) {
            System.out.println("User not found.");
            return null;
        }

        // Create a User object and set its fields
        Customer user = new Customer();
        user.setId(doc.getObjectId("_id").toHexString()); // Convert ObjectId to string
        user.setUsername(doc.getString("username"));
        user.setPassword(doc.getString("password"));
        user.setPhoneNumber(doc.getString("phoneNumber"));
        user.setAddress(doc.getString("address"));
        user.setEmail(doc.getString("email"));

        return user;
    }
    public User get_user_by_id(String id){
        Document doc = collection.find(Filters.eq("id", id)).first();

        // Check if the document exists
        if (doc == null) {
            System.out.println("User not found.");
            return null;
        }

        // Create a User object and set its fields
        User user = new User();
        user.setId(doc.getObjectId("_id").toHexString()); // Convert ObjectId to string
        user.setUsername(doc.getString("username"));
        user.setPassword(doc.getString("password"));
        user.setPhoneNumber(doc.getString("phoneNumber"));
        user.setAddress(doc.getString("address"));
        user.setEmail(doc.getString("email"));

        return user;
    }

    private User getUserByFilter(Document filter) {
        Document doc = collection.find(filter).first();
        return doc != null ? User.fromDocument(doc) : null;
    }

    public void addReview(String bookTitle, String userId, String review) {
        try {
            // Create the review object
            Document reviewdoc = new Document()
                    .append("userId", userId)
                    .append("bookTitle", bookTitle)
                    .append("comment", review);

            var result = reviewCollection.insertOne(reviewdoc); // Insert the review as a new document


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
