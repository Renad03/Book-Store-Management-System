package com.example.bookstoregui;

import com.example.bookstoregui.Database.UserCRUD;
import com.example.bookstoregui.Model.User;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SignupScreen {
    private Stage stage;
    private GridPane layout;
    UserCRUD userCRUD = new UserCRUD();

    public SignupScreen(Stage stage) {
        this.stage = stage;
        layout = new GridPane();

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label phoneLabel = new Label("Phone:");
        TextField phoneField = new TextField();
        Label addressLabel = new Label("Address:");
        TextField addressField = new TextField();
        Button signupButton = new Button("Sign Up");
        Button backButton = new Button("Back to Login");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Failed Signup");
        alert.setHeaderText("This user already exists");

        signupButton.setOnAction(e -> {
            if(userCRUD.getUserByUsername(usernameField.getText()) == null){
               User user1=  User.signup(usernameField.getText(), passwordField.getText(),phoneField.getText(),emailField.getText(), addressField.getText());
                showHomePage(user1);
            }
            else{
                alert.showAndWait();
            }
        });

        backButton.setOnAction(e -> {
            //showLoginScreen();
        });

        layout.add(usernameLabel, 0, 0);
        layout.add(usernameField, 1, 0);
        layout.add(passwordLabel, 0, 1);
        layout.add(passwordField, 1, 1);
        layout.add(emailLabel, 0 , 2);
        layout.add(emailField, 1,2);
        layout.add(phoneLabel, 0 , 3);
        layout.add(phoneField, 1, 3);
        layout.add(addressLabel,0,4);
        layout.add(addressField,1,4);
        layout.add(signupButton, 1, 5);
        layout.add(backButton, 1, 6);
    }

    private void showHomePage(User user) {
        HomePage homePage = new HomePage(stage, user);
        Scene scene = new Scene(homePage.getLayout(), 600, 400);
        stage.setScene(scene);
    }
    public GridPane getLayout() {
        return layout;
    }
}