package com.example.bookstoregui;

import com.example.bookstoregui.Database.OrderCRUD;
import com.example.bookstoregui.Model.Order;
import com.example.bookstoregui.Model.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class MyOrdersScreen extends Application {

    private User user;
    private List<Order> orders;
    private OrderCRUD orderCRUD = new OrderCRUD();
    private ListView<Order> pendingListView, acceptedListView, rejectedListView, deliveredListView;

    public MyOrdersScreen(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) {
        Parent layout = getLayout();  // Get the layout by calling getLayout()
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("My Orders");
        primaryStage.show();
    }

    public Parent getLayout() {
        // Fetch orders by userId using OrderCRUD
        orders = orderCRUD.getOrdersByUserId(user.getId());

        // Create ListViews for each status
        pendingListView = createOrderListView("Pending");
        acceptedListView = createOrderListView("Approved");
        rejectedListView = createOrderListView("Canceled");
        deliveredListView = createOrderListView("Delivered");

        // Create a Refresh Button (optional for updating the orders list)
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> refreshOrderList());

        // Layout with VBox
        VBox vbox = new VBox(pendingListView, acceptedListView, rejectedListView, deliveredListView, refreshButton);
        vbox.setSpacing(10);
        return vbox;
    }

    // Method to create a ListView for orders with a specific status
    private ListView<Order> createOrderListView(String status) {
        ObservableList<Order> filteredOrders = FXCollections.observableArrayList(
                orders.stream()
                        .filter(order -> order.getStatus().equals(status))
                        .collect(Collectors.toList())
        );

        ListView<Order> listView = new ListView<>(filteredOrders);
        listView.setCellFactory(param -> new OrderCell());

        return listView;
    }

    // Refresh the order lists (for example, if the status is updated)
    private void refreshOrderList() {
        pendingListView.setItems(FXCollections.observableArrayList(getOrdersByStatus("Pending")));
        acceptedListView.setItems(FXCollections.observableArrayList(getOrdersByStatus("Approved")));
        rejectedListView.setItems(FXCollections.observableArrayList(getOrdersByStatus("Canceled")));
        deliveredListView.setItems(FXCollections.observableArrayList(getOrdersByStatus("Delivered")));
    }

    // Method to get orders by status
    private List<Order> getOrdersByStatus(String status) {
        return orders.stream()
                .filter(order -> order.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    // Custom cell to display order information
    private static class OrderCell extends javafx.scene.control.ListCell<Order> {
        @Override
        protected void updateItem(Order item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText("Order ID: " + item.getId() + " - " + item.getBooks() +
                          " - $" + item.getTotalPrice());
            }
        }
    }

    public static void main(String[] args) {
        // Pass a user ID for testing
        launch(args);
    }
}
