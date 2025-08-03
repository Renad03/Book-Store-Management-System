package com.example.bookstoregui;

import com.example.bookstoregui.Database.BookCRUD;
import com.example.bookstoregui.Database.CartCRUD;
import com.example.bookstoregui.Model.Book;
import com.example.bookstoregui.Model.Cart;
import com.example.bookstoregui.Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class HomePage {
    private Stage stage;
    private VBox layout;
    private BookCRUD bookCRUD = new BookCRUD();
    private CartCRUD cartCRUD = new CartCRUD();
    User user1;
    public HomePage(Stage stage, User user) {
        this.stage = stage;
        layout = new VBox(10); // Vertical box with spacing of 10
        user1 = user;
        Label welcomeLabel = new Label("Welcome to the Online Bookstore!");
        TextField searchField = new TextField();
        searchField.setPromptText("Search books...");
        Button viewCartButton = new Button("View Cart");
        Button viewOrdersButton = new Button("View My Orders");
        Button logoutButton = new Button("Logout");

        logoutButton.setOnAction(e -> showLoginScreen());
         viewCartButton.setOnAction(e->showMyCartScreen());
         viewOrdersButton.setOnAction(e->showMyOrdersScreen());
        // Fetch all books
        List<Book> books = bookCRUD.getAllBooks();
        VBox booksTable = showBooks(books, searchField, user);

        layout.getChildren().addAll(welcomeLabel, searchField, viewCartButton, viewOrdersButton, logoutButton, booksTable);
    }

    public VBox getLayout() {
        return layout;
    }

    private void showLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(stage);
        Scene scene = new Scene(loginScreen.getLayout(), 600, 400);
        stage.setScene(scene);
    }

    private void showMyCartScreen(){
        MyCartScreen myCartScreen = new MyCartScreen(cartCRUD.getCartByUserId(user1.getId()), user1);
        Scene scene = new Scene(myCartScreen.getLayout(), 600, 400);
        stage.setScene(scene);
    }

    private void showMyOrdersScreen(){
        MyOrdersScreen myOrdersScreen = new MyOrdersScreen(user1);
        Scene scene = new Scene(myOrdersScreen.getLayout(), 600, 400);
        stage.setScene(scene);
    }

    private VBox showBooks(List<Book> books, TextField searchField, User user) {
        // Create a TableView
        TableView<Book> table = new TableView<>();

        // Create columns for Title, Author, and Price
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAuthor()));

        TableColumn<Book, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getPrice()));

        // Create Action Column for "Add to Cart"
        TableColumn<Book, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button addButton = new Button("Add to Cart");

            {
                addButton.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    Cart cart = cartCRUD.getCartByUserId(user.getId());
                    cartCRUD.addBookToCart(book.getTitle(), cart); // Add book to cart
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book added to cart!", ButtonType.OK);
                    alert.showAndWait();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addButton);
                }
            }
        });

        // Create Action Column for "Remove From Cart"
        TableColumn<Book, Void> action2Column = new TableColumn<>("Action");
        action2Column.setCellFactory(col -> new TableCell<>() {
            private final Button removeButton = new Button("Remove From Cart");

            {
                removeButton.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    Cart cart = cartCRUD.getCartByUserId(user.getId());
                    List<String> booksInCart = cart.getBookName();
                    if (booksInCart.contains(book.getTitle())) {
                        cartCRUD.removeBookFromCart(book.getTitle(), cart);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book removed from cart!", ButtonType.OK);
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "This book is not in the cart.", ButtonType.OK);
                        alert.showAndWait();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                }
            }
        });

        // Add columns to the table
        table.getColumns().addAll(titleColumn, authorColumn, priceColumn, actionColumn, action2Column);

        // Populate the table with data
        ObservableList<Book> observableBooks = FXCollections.observableArrayList(books);
        FilteredList<Book> filteredBooks = new FilteredList<>(observableBooks, b -> true);

        // Add a listener to the search field to filter the table
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredBooks.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return book.getTitle().toLowerCase().contains(lowerCaseFilter)
                        || book.getAuthor().toLowerCase().contains(lowerCaseFilter);
            });
        });

        table.setItems(filteredBooks);

        // Create a VBox to hold the table
        return new VBox(table);
    }
}
