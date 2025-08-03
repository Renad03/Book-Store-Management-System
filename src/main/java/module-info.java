module com.example.bookstoregui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires io.github.cdimascio.dotenv.java;
    requires java.datatransfer;


    opens com.example.bookstoregui to javafx.fxml;
    exports com.example.bookstoregui;
}