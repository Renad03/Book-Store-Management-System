package com.example.bookstoregui.Database;

import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.example.bookstoregui.Database.OrderCRUD;
import com.example.bookstoregui.Model.Book;
import com.example.bookstoregui.Model.Order;
import com.example.bookstoregui.Model.User;

import java.util.ArrayList;
import java.util.List;

public class OrderCRUD {

    private final MongoCollection<Document> collection;
    public OrderCRUD() {
        Database database = Database.getInstance();
        this.collection = database.getCollection("order");
    }

    // Create an Order
    public void createOrder(Order order) {

        // Add order details, including userID and other fields
        Document document = new Document("userID", order.getUserID())
                .append("books", order.getBooks())
                .append("status", order.getStatus())
                .append("totalPrice", order.getTotalPrice());

        // Insert the order into the collection
        collection.insertOne(document);
    }


    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        for (Document doc : collection.find()) {
            orders.add(Order.fromDocument(doc));
        }
        return orders;
    }

    public List<Order> getOrdersByUserId(String userId) {
        // Ensure the userId is not null or empty
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }

        // Create a filter to find orders for the given user ID
        Document filter = new Document("userID", userId);

        // Create a list to store the matching orders
        List<Order> orders = new ArrayList<>();

        // Iterate over the filtered documents and convert them to Order objects
        collection.find(filter).forEach(doc -> orders.add(Order.fromDocument(doc)));

        return orders;
    }

    private Order getOrderByFilter(Document filter) {
        Document doc = collection.find(filter).first();
        return doc != null ? Order.fromDocument(doc) : null;
    }

    public Order getOrderById(String orderId) {
        Document filter = new Document("_id", new ObjectId(orderId));
        return getOrderByFilter(filter);
    }

    public List<Order> getOrderByStatus(String status) {
        // Create a filter to match documents with the given status
        Document filter = new Document("status", status);

        // Create a list to store the matching orders
        List<Order> ordersList = new ArrayList<>();

        // Iterate over the filtered documents and convert them to Order objects
        collection.find(filter).forEach(doc -> ordersList.add(Order.fromDocument(doc)));

        return ordersList;
    }



    public void updateOrder(Order order, String newStatus) {
        // Ensure the newStatus is not null or empty
        if (newStatus == null || newStatus.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty.");
        }

        // Create a filter to find the order by its _id
        Document filter = new Document("_id", new ObjectId(order.getId()));

        // Create the update operation to set the new status
        Document update = new Document("$set", new Document("status", newStatus));

        // Perform the update operation
        UpdateResult result = collection.updateOne(filter, update);

        if (result.getMatchedCount() == 0) {
            System.out.println("No order found with the given orderId: " + order.getId());
        } else {
            System.out.println("Order status updated successfully.");
        }
    }


    public void updateOrderStatus(String orderId, String newStatus) {
        // Ensure the newStatus is not null or empty
        if (newStatus == null || newStatus.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty.");
        }

        // Create a filter to find the order by its _id
        Document filter = new Document("_id", new ObjectId(orderId));

        // Create the update operation to set the new status
        Document update = new Document("$set", new Document("status", newStatus));

        // Perform the update operation
        UpdateResult result = collection.updateOne(filter, update);

        if (result.getMatchedCount() == 0) {
            System.out.println("No order found with the given orderId: " + orderId);
        } else {
            System.out.println("Order status updated successfully.");
        }
    }


    public void deleteOrder(Order order) {
        collection.deleteOne(Order.toDocument(order));
    }

}