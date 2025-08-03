package com.example.bookstoregui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Online Bookstore");
        showLoginScreen(primaryStage);
        primaryStage.show();
    }

    private void showLoginScreen(Stage stage) {
        LoginScreen loginScreen = new LoginScreen(stage);
        Scene scene = new Scene(loginScreen.getLayout(), 400, 300);
        stage.setScene(scene);
    }


    public static void main(String[] args) {
        launch();
    }
}