package com.example.bookstoregui.Database;

import com.example.bookstoregui.Model.Book;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import com.example.bookstoregui.Model.Cart;
import com.mongodb.client.model.Updates;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartCRUD {
    private final MongoCollection<Document> cartCollection;
    private final MongoCollection<Document> booksCollection;

    public CartCRUD() {
        // Initialize the collection for Cart
        Database database = Database.getInstance();
        this.cartCollection = database.getCollection("cart");
        this.booksCollection = database.getCollection("book");
    }

    // Create a new cart entry
    public void createCart(String userID) {
        Cart cart = new Cart();
        cart.setUserID(userID);
        Document cartDoc = Cart.toDocument(cart);
        cartCollection.insertOne(cartDoc);
        System.out.println("Cart created successfully.");
    }

    // Add books to a cart
    public void addBookToCart(String bookName, Cart cart) {
        if (bookName == null || bookName.isEmpty()) {
            throw new IllegalArgumentException("Book name cannot be null or empty.");
        }

        // Prepare the update operation to add the book to the cart
        Document update = new Document("$push", new Document("books", bookName));

        // Update the cart in the database
        cartCollection.updateOne(
                Filters.eq("_id", new ObjectId(cart.getId())), // Find the cart by its ID
                update // Add the book to the "books" array
        );

        System.out.println("Book added to cart successfully.");
    }

    // Remove books from a cart
    public void removeBookFromCart(String bookName, Cart cart1) {
        String cartId = cart1.getId();
        // Find the cart document by its ID
        Document cart = cartCollection.find(Filters.eq("_id", new ObjectId(cartId))).first();

        if (cart != null && cart.containsKey("books")) {
            // Get the list of books from the cart
            @SuppressWarnings("unchecked")
            List<String> books = (List<String>) cart.get("books");

            // Remove only the first occurrence of the book
            if (books.remove(bookName)) {
                // Update the cart with the modified books array
                cartCollection.updateOne(
                        Filters.eq("_id", new ObjectId(cartId)),
                        new Document("$set", new Document("books", books))
                );
            } else {
                System.out.println("Book not found in the cart.");
            }
        } else {
            System.out.println("Cart not found or does not contain books.");
        }
    }





    public Cart getCartByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }

        // Query the database for the cart by userId
        Document cartDoc = cartCollection.find(Filters.eq("userID", userId)).first();

        if (cartDoc == null) {
            System.out.println("Cart not found for user ID: " + userId);
            return null;
        }

        // Create and return a Cart object from the document
        Cart cart = new Cart();
        cart.setId(cartDoc.getObjectId("_id").toHexString());
        cart.setUserID(cartDoc.getString("userID"));
        cart.setBookName(cartDoc.getList("books", String.class)); // Assuming "books" is a list of book names

        return cart;
    }

    public List<Book> getBooksInCart(String cartId) {
        List<Book> books = new ArrayList<>();
        try {
            // Find the cart by its ID
            Document cart = cartCollection.find(Filters.eq("_id", new ObjectId(cartId))).first();

            if (cart != null && cart.containsKey("books")) {
                @SuppressWarnings("unchecked")
                List<String> bookTitles = (List<String>) cart.get("books");

                // Retrieve the books from the booksCollection using their titles
                try (MongoCursor<Document> cursor = booksCollection.find(Filters.in("title", bookTitles)).iterator()) {
                    while (cursor.hasNext()) {
                        Document doc = cursor.next();
                        // Map Document to Book object
                        Book book = new Book(
                                doc.getString("title"),
                                doc.getString("author"),
                                doc.getString("stock"),
                                doc.getString("price"),
                                doc.getString("rating"),
                                doc.getInteger("sales"),
                                doc.getString("categorie_Type")// Default to 0 if "quantity" is not present
                        );
                        books.add(book);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

}
