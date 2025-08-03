package com.example.bookstoregui.Model;

import org.bson.Document;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private List<String> books;
    private String status;
    private BigDecimal totalPrice;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    private String userID;
    // Getters and Setters
    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        this.books = books;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Constructors
    public Order(List<String> books, BigDecimal totalPrice, String userID) {
        this.books = books;
        this.status = "Pending";
        this.totalPrice = totalPrice;
        this.userID = userID;
    }

    public Order() {

    }

    public static Document toDocument(Order order) {
        return new Document()
                .append("userID", order.getUserID()) // Include userID
                .append("items", order.getBooks())
                .append("status", order.getStatus())
                .append("totalPrice", new Decimal128(order.getTotalPrice())); // Convert BigDecimal to Decimal128
    }


    public static Order fromDocument(Document doc) {

        List<String> items = (List<String>) doc.get("books"); // Retrieve List<String> directly
        String status = doc.getString("status");
        Decimal128 decimal128 = doc.get("totalPrice", Decimal128.class); // Retrieve as Decimal128
        BigDecimal totalPrice = decimal128.bigDecimalValue(); // Convert to BigDecimal

        // Ensure userID is being retrieved
        String userId = doc.getString("userID");

        // Ensure the order ID is being retrieved correctly
        String orderId = doc.getObjectId("_id").toHexString(); // Using _id as the orderId

        Order order = new Order(items, totalPrice, userId);
        order.setStatus(status);
        order.setUserID(userId); // Set the userID
        order.setId(orderId); // Set the order ID

        return order;
    }


    @Override
    public String toString() {
        return "Order{" +
                "userID='" + userID + '\'' + // Display userID
                ", books=" + books + // List of book names
                ", status='" + status + '\'' + // Order status
                ", totalPrice=" + totalPrice + // Total price of the order
                '}';
    }

}