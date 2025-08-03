package com.example.bookstoregui;

import com.example.bookstoregui.Database.BookCRUD;
import com.example.bookstoregui.Database.OrderCRUD;
import com.example.bookstoregui.Model.*;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class AdminPage extends Application {

    private final Admin admin = new Admin ("admin", "password", "1234567890", "123 Main St", "admin@example.com");
    BookCRUD crud=new BookCRUD ();
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle ("Admin Panel");

        // Root layout
        VBox root = new VBox (10);

        // Buttons for functionalities
        Button addBookButton = new Button ("Add Book");
        Button editBookButton = new Button ("Edit Book");
        Button deleteBookButton = new Button ("Delete Book");
        Button trackStockButton = new Button ("Track Stock");
        Button updateStockButton = new Button ("Update Book Availability");
        Button topSellingButton = new Button ("Top Selling Books");
        Button popularCategoriesButton = new Button ("Popular Categories");
        Button viewOrdersButton = new Button ("View Orders");
        Button confirmCancelOrdersButton = new Button ("Confirm/Cancel Orders");
        Button DeliveredOrderButton = new Button ("Delivered Orders");

        // Add buttons to the layout
        root.getChildren ().addAll (addBookButton, editBookButton, deleteBookButton, trackStockButton,
                updateStockButton, topSellingButton, popularCategoriesButton, viewOrdersButton, confirmCancelOrdersButton,DeliveredOrderButton);

        // Button actions
        addBookButton.setOnAction (e -> showAddBookDialog ());
        editBookButton.setOnAction (e -> showEditBookDialog ());
        deleteBookButton.setOnAction (e -> showDeleteBookDialog ());
        trackStockButton.setOnAction (e -> showStock ());
        updateStockButton.setOnAction(e -> updateBookAvailability());
        topSellingButton.setOnAction (e -> showTopSellingBooks ());
        popularCategoriesButton.setOnAction (e -> showPopularCategories ());
        viewOrdersButton.setOnAction (e -> showOrders ());
        confirmCancelOrdersButton.setOnAction(e ->order_status ());
        DeliveredOrderButton.setOnAction(e -> order_delivered());

        // Scene and Stage setup
        Scene scene = new Scene (root, 400, 500);
        primaryStage.setScene (scene);
        primaryStage.show ();
    }

    // Method to show the Add Book Dialog
    private void showAddBookDialog() {
        // New Stage for Add Book Dialog
        Stage addBookStage = new Stage ();
        addBookStage.setTitle ("Add New Book");

        TextField titleField = new TextField ();
        titleField.setPromptText ("Enter Title");

        TextField authorField = new TextField ();
        authorField.setPromptText ("Enter Author");

        TextField stockField = new TextField ();
        stockField.setPromptText ("Enter Stock");

        TextField priceField = new TextField ();
        priceField.setPromptText ("Enter Price");

        TextField ratingField = new TextField ();
        ratingField.setPromptText ("Enter Rating");

        TextField categoryField = new TextField ();
        categoryField.setPromptText ("Enter Category");

        Button createButton = new Button ("Create Book");
        createButton.setOnAction (e -> {
            String title = titleField.getText ();
            String author = authorField.getText ();
            String stock = stockField.getText ();
            String price = priceField.getText ();
            String rating = ratingField.getText ();
            String category = categoryField.getText ();

            if (title.isEmpty () || author.isEmpty () || stock.isEmpty () || price.isEmpty () || rating.isEmpty () || category.isEmpty ()) {
                showAlert ("Error", "All fields must be filled!", Alert.AlertType.ERROR);
                return;
            }

            try {
                int stockValue = Integer.parseInt (stock);
                double priceValue = Double.parseDouble (price);
                double ratingValue = Double.parseDouble (rating);

                admin.addBook (title, author, stock, price, rating, category);
                showAlert ("Success", "Book added successfully!", Alert.AlertType.INFORMATION);
                addBookStage.close (); // Close the dialog after successful creation
            } catch (NumberFormatException ex) {
                showAlert ("Error", "Invalid stock, price, or rating value.", Alert.AlertType.ERROR);
            }
        });

        VBox vbox = new VBox (10, titleField, authorField, stockField, priceField, ratingField, categoryField, createButton);
        Scene scene = new Scene (vbox, 400, 400);

        addBookStage.setScene (scene);
        addBookStage.show ();
    }

    // Method to show the Edit Book Dialog
    private void showEditBookDialog() {
        // New Stage for Edit Book Dialog
        Stage editBookStage = new Stage ();
        editBookStage.setTitle ("Edit Book");

        TextField bookNameField = new TextField ();
        bookNameField.setPromptText ("Enter book name to edit");

        TextField newTitleField = new TextField ();
        newTitleField.setPromptText ("Enter new title");

        TextField newAuthorField = new TextField ();
        newAuthorField.setPromptText ("Enter new author");

        TextField newStockField = new TextField ();
        newStockField.setPromptText ("Enter new stock");

        TextField newPriceField = new TextField ();
        newPriceField.setPromptText ("Enter new price");

        TextField newRatingField = new TextField ();
        newRatingField.setPromptText ("Enter new rating");

        TextField newCategoryField = new TextField ();
        newCategoryField.setPromptText ("Enter new category");

        Button updateButton = new Button ("Update Book");
        updateButton.setOnAction (e -> {
            String bookName = bookNameField.getText ();
            String newTitle = newTitleField.getText ();
            String newAuthor = newAuthorField.getText ();
            String newStock = newStockField.getText ();
            String newPrice = newPriceField.getText ();
            String newRating = newRatingField.getText ();
            String newCategory = newCategoryField.getText ();

            if (bookName.isEmpty () || newTitle.isEmpty () || newAuthor.isEmpty () || newStock.isEmpty () || newPrice.isEmpty () || newRating.isEmpty () || newCategory.isEmpty ()) {
                showAlert ("Error", "All fields must be filled!", Alert.AlertType.ERROR);
                return;
            }

            try {
                int stockValue = Integer.parseInt (newStock);
                double priceValue = Double.parseDouble (newPrice);
                double ratingValue = Double.parseDouble (newRating);

                admin.editBook (bookName, newTitle, newAuthor, newStock, newPrice, newRating, newCategory, 0);
                showAlert ("Success", "Book updated successfully!", Alert.AlertType.INFORMATION);
                editBookStage.close ();
            } catch (NumberFormatException ex) {
                showAlert ("Error", "Invalid stock, price, or rating value.", Alert.AlertType.ERROR);
            }
        });

        VBox vbox = new VBox (10, bookNameField, newTitleField, newAuthorField, newStockField, newPriceField, newRatingField, newCategoryField, updateButton);
        Scene scene = new Scene (vbox, 400, 400);

        editBookStage.setScene (scene);
        editBookStage.show ();
    }

    // Method to show the Delete Book Dialog
    private void showDeleteBookDialog() {
        // New Stage for Delete Book Dialog
        Stage deleteBookStage = new Stage ();
        deleteBookStage.setTitle ("Delete Book");

        TextField bookNameField = new TextField ();
        bookNameField.setPromptText ("Enter book name to delete");
        BookCRUD bookCRUD = new BookCRUD();
        Button deleteButton = new Button ("Delete Book");
        deleteButton.setOnAction (e -> {
            String bookName = bookNameField.getText ();
            if (bookName.isEmpty ()) {
                showAlert ("Error", "Book name cannot be empty!", Alert.AlertType.ERROR);
            }
            else if(bookCRUD.getBookByTitle(bookName) == null){
                showAlert ("Error", "This book doesn't exist!", Alert.AlertType.ERROR);
            }
            else {
                admin.deleteBook (bookName);
                showAlert ("Success", "Book deleted successfully!", Alert.AlertType.INFORMATION);
                deleteBookStage.close ();
            }
        });

        VBox vbox = new VBox (10, bookNameField, deleteButton);
        Scene scene = new Scene (vbox, 400, 300);

        deleteBookStage.setScene (scene);
        deleteBookStage.show ();
    }

    // Method to show the Update Stock Dialog
    private void showUpdateStockDialog() {
        Stage updateStockStage = new Stage ();
        updateStockStage.setTitle ("Update Book Stock");

        TextField bookNameField = new TextField ();
        bookNameField.setPromptText ("Enter book name to update stock");

        TextField newStockField = new TextField ();
        newStockField.setPromptText ("Enter new stock");

        Button updateButton = new Button ("Update Stock");
        updateButton.setOnAction (e -> {
            String bookName = bookNameField.getText ();
            String newStock = newStockField.getText ();

            if (bookName.isEmpty () || newStock.isEmpty ()) {
                showAlert ("Error", "Both fields must be filled!", Alert.AlertType.ERROR);
                return;
            }

            admin.updateBookAvailability (bookName, newStock);
            updateStockStage.close ();
        });

        VBox vbox = new VBox (10, bookNameField, newStockField, updateButton);
        Scene scene = new Scene (vbox, 400, 300);

        updateStockStage.setScene (scene);
        updateStockStage.show ();
    }

    private void showStock() {
        List<Book> bookInventory = admin.trackStock (); // Get all books

        // Create a TableView to display the books
        TableView<Book> tableView = new TableView<> ();

        // Create columns for the table
        TableColumn<Book, String> titleColumn = new TableColumn<> ("Book Title");
        titleColumn.setCellValueFactory (cellData -> new SimpleStringProperty (cellData.getValue ().getTitle ()));

        TableColumn<Book, String> stockColumn = new TableColumn<> ("Stock");
        stockColumn.setCellValueFactory (cellData -> new SimpleStringProperty (cellData.getValue ().getStock ()));

        // Add columns to the TableView
        tableView.getColumns ().add (titleColumn);
        tableView.getColumns ().add (stockColumn);

        // Set the items in the table (the list of books)
        tableView.setItems (javafx.collections.FXCollections.observableArrayList (bookInventory));

        // Create a VBox to hold the TableView
        VBox vbox = new VBox (10);
        vbox.getChildren ().add (tableView);

        // Create a new Scene with the TableView
        Scene scene = new Scene (vbox, 600, 400);

        // Create a new Stage for showing the stock information
        Stage stockStage = new Stage ();
        stockStage.setTitle ("Stock Information");
        stockStage.setScene (scene);
        stockStage.show ();
    }

    public void showTopSellingBooks() {
        Stage topSellingStage = new Stage ();
        topSellingStage.setTitle ("Top Selling Books");

        // Create the TableView
        TableView<Book> tableView = new TableView<> ();

        // Create columns for the table
        TableColumn<Book, String> titleColumn = new TableColumn<> ("Book Title");
        titleColumn.setCellValueFactory (cellData -> new SimpleStringProperty (cellData.getValue ().getTitle ()));

        TableColumn<Book, String> authorColumn = new TableColumn<> ("Author");
        authorColumn.setCellValueFactory (cellData -> new SimpleStringProperty (cellData.getValue ().getAuthor ()));

        TableColumn<Book, Integer> salesColumn = new TableColumn<> ("Sales");
        salesColumn.setCellValueFactory (cellData -> new SimpleIntegerProperty (cellData.getValue ().getSales ()).asObject ());

        // Add columns to the TableView
        tableView.getColumns ().addAll (titleColumn, authorColumn, salesColumn);

        // Get the top-selling books and set them to the TableView
        List<Book> topBooks = admin.topSelling ();
        tableView.getItems ().setAll (topBooks);

        // Create a layout and add the TableView to it
        VBox vbox = new VBox (10, tableView);
        Scene scene = new Scene (vbox, 500, 400);

        topSellingStage.setScene (scene);
        topSellingStage.show ();
    }

    public void showPopularCategories() {
        // Create a new Stage to display the categories
        Stage popularCategoriesStage = new Stage ();
        popularCategoriesStage.setTitle ("Popular Categories");

        // Create a TableView to display the top categories and their quantities
        TableView<AbstractMap.SimpleEntry<String, Integer>> tableView = new TableView<> ();

        // Create the category column
        TableColumn<AbstractMap.SimpleEntry<String, Integer>, String> categoryColumn = new TableColumn<> ("Category");
        categoryColumn.setCellValueFactory (cellData -> new SimpleStringProperty (cellData.getValue ().getKey ()));

        // Create the quantity column
        TableColumn<AbstractMap.SimpleEntry<String, Integer>, Integer> quantityColumn = new TableColumn<> ("Quantity");
        quantityColumn.setCellValueFactory (cellData -> new SimpleIntegerProperty (cellData.getValue ().getValue ()).asObject ());

        // Add columns to the TableView
        tableView.getColumns ().addAll (categoryColumn, quantityColumn);

        // Get the top 3 popular categories
        List<AbstractMap.SimpleEntry<String, Integer>> topCategories = admin.getPopularCategories ();
        tableView.getItems ().setAll (topCategories); // Populate the table with the top categories

        // Layout and Scene setup
        VBox vbox = new VBox (10, tableView);
        Scene scene = new Scene (vbox, 500, 400);

        popularCategoriesStage.setScene (scene);
        popularCategoriesStage.show ();
    }

    public void showOrders() {
        // Create a new Stage to display the orders
        Stage ordersStage = new Stage ();
        ordersStage.setTitle ("Orders List");

        // Create a TableView to display the list of orders
        TableView<Order> tableView = new TableView<> ();

        // Create the columns for the TableView
        TableColumn<Order, String> orderIdColumn = new TableColumn<> ("Order ID");
        orderIdColumn.setCellValueFactory (cellData ->
                new SimpleStringProperty (String.valueOf (cellData.getValue ().getId ())));

        TableColumn<Order, Double> totalColumn = new TableColumn<> ("Total");
        totalColumn.setCellValueFactory (cellData ->
                new SimpleDoubleProperty (cellData.getValue ().getTotalPrice ().doubleValue ()).asObject ());

        TableColumn<Order, String> StatusColumn = new TableColumn<> ("Status");
        StatusColumn.setCellValueFactory (cellData ->
                new SimpleStringProperty (String.valueOf (cellData.getValue ().getStatus ())));

        TableColumn<Order, String> booksColumn = new TableColumn<> ("Books");
        booksColumn.setCellValueFactory (cellData -> {
            List<String> books = cellData.getValue ().getBooks ();
            StringBuilder bookTitles = new StringBuilder ();
            for (String book : books) {
                if (bookTitles.length () > 0) {
                    bookTitles.append (", "); // Separate book titles with commas
                }
                bookTitles.append (book); // Add each book title to the string
            }
            return new SimpleStringProperty (bookTitles.toString ()); // Return as String
        });

        // Add the columns to the TableView
        tableView.getColumns ().addAll (orderIdColumn, totalColumn, booksColumn, StatusColumn);

        // Get the list of orders
        List<Order> orders = admin.viewOrders ();
        tableView.getItems ().setAll (orders); // Populate the table with the orders

        // Layout and Scene setup
        VBox vbox = new VBox (10, tableView);
        Scene scene = new Scene (vbox, 600, 400);

        ordersStage.setScene (scene);
        ordersStage.show ();
    }
    public void updateBookAvailability() {
        // Input dialog to get the book name from the user
        TextInputDialog bookNameDialog = new TextInputDialog();
        bookNameDialog.setTitle("Enter Book Name");
        bookNameDialog.setHeaderText("Find Book");
        bookNameDialog.setContentText("Enter the book name:");

        Optional<String> bookNameInput = bookNameDialog.showAndWait();
        if (bookNameInput.isPresent()) {
            String bookName = bookNameInput.get();

            // Get the book by title
            Book book = crud.getBookByTitle(bookName);

            if (book != null) {
                // Display current stock
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Book Availability");
                infoAlert.setHeaderText("Book: " + bookName);
                infoAlert.setContentText("Current Stock: " + book.getStock() +
                        "\nAvailability: " + (book.getStock().equals("0") ? "Not Available" : "Available"));
                infoAlert.showAndWait();

                // Confirm if the user wants to update the stock
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Update Stock Confirmation");
                confirmAlert.setHeaderText("Do you want to update the stock for " + bookName + "?");
                confirmAlert.setContentText("Click 'OK' to proceed or 'Cancel' to abort.");
                Optional<ButtonType> result = confirmAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Input dialog for new stock
                    TextInputDialog inputDialog = new TextInputDialog();
                    inputDialog.setTitle("Enter New Stock");
                    inputDialog.setHeaderText("Update Stock for " + bookName);
                    inputDialog.setContentText("Enter the new stock value:");

                    Optional<String> newStockInput = inputDialog.showAndWait();
                    if (newStockInput.isPresent()) {
                        String newStock = newStockInput.get();

                        // Validate new stock input
                        if (newStock.matches("\\d+") && Integer.parseInt(newStock) >= 0) {
                            // Create an updated book object
                            Book updatedBook = new Book(
                                    book.getTitle(),
                                    book.getAuthor(),
                                    newStock,
                                    book.getPrice(),
                                    book.getRating(),
                                    book.getSales(),
                                    book.getCategorieType()
                            );

                            // Update the book in CRUD
                            crud.updateBook(book, updatedBook);

                            // Success alert
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Success");
                            successAlert.setHeaderText("Stock Updated Successfully");
                            successAlert.setContentText("New Stock: " + newStock);
                            successAlert.showAndWait();
                        } else {
                            // Invalid input alert
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Invalid Input");
                            errorAlert.setHeaderText("Update Failed");
                            errorAlert.setContentText("Please enter a valid non-negative integer for the stock.");
                            errorAlert.showAndWait();
                        }
                    }
                }
            } else {
                // Book not found alert
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Book Not Found");
                errorAlert.setHeaderText("Error");
                errorAlert.setContentText("The book '" + bookName + "' could not be found.");
                errorAlert.showAndWait();
            }
        }
    }


    public void order_status() {

        Stage ordersStage = new Stage ();
        ordersStage.setTitle ("Orders List");
        OrderCRUD ordercrud =new OrderCRUD ();
        // Create a TableView to display the list of orders
        TableView<Order> tableView = new TableView<> ();

        // Create the columns for the TableView
        TableColumn<Order, String> orderIdColumn = new TableColumn<> ("Order ID");
        orderIdColumn.setCellValueFactory (cellData ->
                new SimpleStringProperty (String.valueOf (cellData.getValue ().getId ())));

        TableColumn<Order, Double> totalColumn = new TableColumn<> ("Total");
        totalColumn.setCellValueFactory (cellData ->
                new SimpleDoubleProperty (cellData.getValue ().getTotalPrice ().doubleValue ()).asObject ());

        TableColumn<Order, String> StatusColumn = new TableColumn<> ("Status");
        StatusColumn.setCellValueFactory (cellData ->
                new SimpleStringProperty (String.valueOf (cellData.getValue ().getStatus ())));

        TableColumn<Order, String> booksColumn = new TableColumn<> ("Books");
        booksColumn.setCellValueFactory (cellData -> {
            List<String> books = cellData.getValue ().getBooks ();
            StringBuilder bookTitles = new StringBuilder ();
            for (String book : books) {
                if (bookTitles.length () > 0) {
                    bookTitles.append (", "); // Separate book titles with commas
                }
                bookTitles.append (book); // Add each book title to the string
            }
            return new SimpleStringProperty (bookTitles.toString ()); // Return as String
        });


        TableColumn<Order, Void> actionColumn = new TableColumn<> ("Approve Orders");
        actionColumn.setCellFactory (param -> new TableCell<> () {
            private final Button actionButton = new Button ("Approve");

            {

                actionButton.setOnAction (event -> {
                    Order order = getTableRow().getItem();
                    if (order != null) {
                        admin.confirmOrCancelOrders ("Approve", order);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem (item, empty);
                if (empty) {
                    setGraphic (null);
                } else {
                    setGraphic (actionButton);
                }
            }
        });

        TableColumn<Order, Void> ActionColumn = new TableColumn<> ("Cancel Orders");
        ActionColumn.setCellFactory (param -> new TableCell<> () {
            private final Button ActionButton = new Button ("Cancel");

            {

                ActionButton.setOnAction (event -> {
                    Order order = getTableRow().getItem();
                    if (order != null) {
                        admin.confirmOrCancelOrders ("Cancel", order);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem (item, empty);
                if (empty) {
                    setGraphic (null);
                } else {
                    setGraphic (ActionButton);
                }
            }
        });
        // Add the columns to the TableView
        tableView.getColumns ().addAll (orderIdColumn, totalColumn, booksColumn, StatusColumn,actionColumn,ActionColumn);

        // Get the list of orders
        List<Order> orders = admin.pending ();
        tableView.getItems ().setAll (orders); // Populate the table with the orders

        // Layout and Scene setup
        VBox vbox = new VBox (10, tableView);
        Scene scene = new Scene (vbox, 600, 400);

        ordersStage.setScene (scene);
        ordersStage.show ();
    }
    public void order_delivered() {

        Stage ordersStage = new Stage ();
        ordersStage.setTitle ("Orders List");
        OrderCRUD ordercrud =new OrderCRUD ();
        // Create a TableView to display the list of orders
        TableView<Order> tableView = new TableView<> ();

        // Create the columns for the TableView
        TableColumn<Order, String> orderIdColumn = new TableColumn<> ("Order ID");
        orderIdColumn.setCellValueFactory (cellData ->
                new SimpleStringProperty (String.valueOf (cellData.getValue ().getId ())));

        TableColumn<Order, Double> totalColumn = new TableColumn<> ("Total");
        totalColumn.setCellValueFactory (cellData ->
                new SimpleDoubleProperty (cellData.getValue ().getTotalPrice ().doubleValue ()).asObject ());

        TableColumn<Order, String> StatusColumn = new TableColumn<> ("Status");
        StatusColumn.setCellValueFactory (cellData ->
                new SimpleStringProperty (String.valueOf (cellData.getValue ().getStatus ())));

        TableColumn<Order, String> booksColumn = new TableColumn<> ("Books");
        booksColumn.setCellValueFactory (cellData -> {
            List<String> books = cellData.getValue ().getBooks ();
            StringBuilder bookTitles = new StringBuilder ();
            for (String book : books) {
                if (bookTitles.length () > 0) {
                    bookTitles.append (", "); // Separate book titles with commas
                }
                bookTitles.append (book); // Add each book title to the string
            }
            return new SimpleStringProperty (bookTitles.toString ()); // Return as String
        });


        TableColumn<Order, Void> actionColumn = new TableColumn<> ("Delivered Orders");
        actionColumn.setCellFactory (param -> new TableCell<> () {
            private final Button actionButton = new Button ("Delivered");

            {

                actionButton.setOnAction (event -> {
                    Order order = getTableRow().getItem();
                    if (order != null) {
                        admin.Delivered_Orders ( order);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem (item, empty);
                if (empty) {
                    setGraphic (null);
                } else {
                    setGraphic (actionButton);
                }
            }
        });


        // Add the columns to the TableView
        tableView.getColumns ().addAll (orderIdColumn, totalColumn, booksColumn, StatusColumn,actionColumn);

        // Get the list of orders
        List<Order> orders = admin.Approved ();
        tableView.getItems ().setAll (orders); // Populate the table with the orders

        // Layout and Scene setup
        VBox vbox = new VBox (10, tableView);
        Scene scene = new Scene (vbox, 600, 400);

        ordersStage.setScene (scene);
        ordersStage.show ();
    }
    // Method to show alert
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert (alertType);
        alert.setTitle (title);
        alert.setHeaderText (null);
        alert.setContentText (message);
        alert.showAndWait ();
    }

    public static void main(String[] args) {
        launch (args);
    }


}