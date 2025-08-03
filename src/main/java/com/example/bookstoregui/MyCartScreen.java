package com.example.bookstoregui;
import com.example.bookstoregui.Database.CartCRUD;
import com.example.bookstoregui.Database.OrderCRUD;
import com.example.bookstoregui.Model.Book;
import com.example.bookstoregui.Model.Cart;
import com.example.bookstoregui.Model.Order;
import com.example.bookstoregui.Model.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MyCartScreen extends Application {

    private Cart cart;
    CartCRUD cartCRUD = new CartCRUD();
    OrderCRUD orderCRUD = new OrderCRUD();
    User user;
    public MyCartScreen(Cart cart, User user) {
        this.cart = cart;
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) {
        Parent layout = getLayout();  // Get the layout by calling getLayout()
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("My Cart");
        primaryStage.show();
    }

    /**
     * This function returns the layout for the MyCartScreen.
     */
    public Parent getLayout() {
        ObservableList<Book> bookList = FXCollections.observableArrayList(cartCRUD.getBooksInCart(cart.getId()));

        // Create a ListView to display the books in the cart
        ListView<Book> listView = new ListView<>(bookList);
        listView.setCellFactory(param -> new BookCell());

        // Create a Remove Button
        Button removeButton = new Button("Remove Selected");
        removeButton.setOnAction(event -> {
            Book selectedBook = listView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                cartCRUD.removeBookFromCart(selectedBook.getTitle(), cart);
                bookList.remove(selectedBook);
            }
        });

        // Calculate the total price of all books in the list


// Create a Checkout Button
        Button checkoutButton = new Button("Checkout");
        checkoutButton.setOnAction(event -> {
            double totalPrice = calculateTotalPrice(bookList); // Use the bookList already passed to the method
            displayTotalPrice(totalPrice);
        });

        // Create a VBox layout and add all the components to it
        VBox vbox = new VBox(listView, removeButton, checkoutButton);
        vbox.setSpacing(10);  // Add some spacing between the elements
        return vbox;  // Return the VBox layout
    }


    private double calculateTotalPrice(ObservableList<Book> bookList) {
        return bookList.stream()
                .mapToDouble(book -> Double.parseDouble(book.getPrice()))
                .sum();
    }


    private void displayTotalPrice(double totalPrice) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText("Total Price");
        alert.setContentText("The total price of the books in your cart is $" + String.format("%.2f", totalPrice));
        Button PlaceOrder = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        PlaceOrder.setText("Proceed to Order");
        PlaceOrder.setOnAction(e -> PlaceOrder(totalPrice));
        alert.showAndWait();
    }
    private void PlaceOrder(double totalPrice){
        List<Book> books = cartCRUD.getBooksInCart(cart.getId());
        List<String> bookTitles = new ArrayList<>();
        for(Book book: books){
            bookTitles.add(book.getTitle());
        }
        BigDecimal price = BigDecimal.valueOf(totalPrice);
        Order order = new Order(bookTitles, price ,user.getId());;
        orderCRUD.createOrder(order);
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Order Placed");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Your order was placed successfully!");
        successAlert.showAndWait();
    }

    // Custom cell to display book information
    private static class BookCell extends javafx.scene.control.ListCell<Book> {
        @Override
        protected void updateItem(Book item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.getTitle() + " - $" + item.getPrice());
            }
        }
    }
}
