package com.example.bookstoregui;
import com.example.bookstoregui.Model.Admin;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.example.bookstoregui.Model.User;
public class LoginScreen {
    private  User user;
    private Stage stage;
    private GridPane layout;

    public LoginScreen(Stage stage) {
        this.stage = stage;
        layout = new GridPane();

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button signupButton = new Button("Sign Up");


        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Failed Login");
        alert.setHeaderText("Invalid Username or Password");



        loginButton.setOnAction(e -> {
          // user = User.login(usernameField.getText(), passwordField.getText());
           user = User.get_user(usernameField.getText());

           if(user!=null){
               showHomePage(user);
           }
            else {
               // Show the alert and wait for the user to close it
               alert.showAndWait();
           }
        });

        signupButton.setOnAction(e -> {
            showSignupScreen();
        });

        layout.add(usernameLabel, 0, 0);
        layout.add(usernameField, 1, 0);
        layout.add(passwordLabel, 0, 1);
        layout.add(passwordField, 1, 1);
        layout.add(loginButton, 1, 2);
        layout.add(signupButton, 1, 3);
    }

    public GridPane getLayout() {
        return layout;
    }

    private void showHomePage(User user) {
        HomePage homePage = new HomePage(stage, user);
        Scene scene = new Scene(homePage.getLayout(), 600, 400);
        stage.setScene(scene);
    }


    private void showSignupScreen() {
        SignupScreen signupScreen = new SignupScreen(stage);
        Scene scene = new Scene(signupScreen.getLayout(), 400, 300);
        stage.setScene(scene);
    }
}