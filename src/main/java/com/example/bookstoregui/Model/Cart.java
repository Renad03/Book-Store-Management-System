package com.example.bookstoregui.Model;
import org.bson.Document;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.util.List;

public class Cart {
    private List<String> bookName;
    //private BigDecimal totalPrice;
    private String userID;
    private String id;
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    /*public Cart() {
        this.totalPrice = BigDecimal.valueOf(0);
    }*/

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<String> getBookName() {
        return bookName;
    }

    public void setBookName(List<String> bookName) {
        this.bookName = bookName;
    }

    /*public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }*/

    public static Document toDocument(Cart cart) {
        return new Document()
                .append("userID", cart.getUserID())
                .append("bookName", cart.getBookName()) ;// bookName is a List<String>, stored directly
               // .append("totalPrice", new Decimal128(cart.getTotalPrice())); // Convert BigDecimal to Decimal128
    }

    public static Cart fromDocument(Document doc) {
        // Retrieve fields from the document
        List<String> bookName = (List<String>) doc.get("bookName"); // Retrieve List<String> directly
        Decimal128 decimal128 = doc.get("totalPrice", Decimal128.class); // Retrieve as Decimal128
        BigDecimal totalPrice = decimal128.bigDecimalValue(); // Convert to BigDecimal
        String userId = (String) doc.get("userID"); // Retrieve userID

        // Create a new Cart object and set its properties
        Cart cart = new Cart();
        cart.setBookName(bookName);
        //cart.setTotalPrice(totalPrice);
        cart.setUserID(userId);

        return cart; // Return the populated Cart object
    }

    @Override
    public String toString() {
        return "Cart{" +
                "userID='" + userID + '\'' +
                ", bookName=" + bookName + // Displaying the list of book names
               // ", totalPrice=" + totalPrice  // Displaying the total price
                '}';
    }

}
