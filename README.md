
# Book Store Management System

A Java-based desktop application for managing a bookstore, built as a course project for the Design Patterns class. The system uses JavaFX for the user interface and MongoDB for data storage.

This project emphasizes the application of Software Design Patterns to create a clean, maintainable, and scalable codebase.

 Key Features

    Add, edit, delete, and search for books in the store.

    Manage different types of books such as:

        Action Books

        Historical Books

        Classic Books

        Educational Books

    Persistent storage with MongoDB.

 Design Patterns Used

 1- Singleton Pattern

A Singleton class is used to manage the MongoDB connection, ensuring a single shared instance is used across the application.

 2- Factory Pattern

The Factory Design Pattern is applied to centralize and simplify the creation of various book types.

    Promotes clean code by avoiding repeated instantiation logic.

    Enhances maintainability and allows for easy extension to new book categories.

    Enables dynamic book creation based on input like bookType.


## Environment Variables

To run this project, you will need to add the following environment variables to your .env file

`DB_CONNECTION_STRING`

`DB_NAME`


## Tech Stack

**Language:** Java 21

**Gui:** Javafx

**Database:** MongoDB


## Authors

- [@Omar Ahmed Salama](https://github.com/omar-7salama)

- [@Eman Mohamed](https://github.com/emanmohamed1710)

- [@Clara Akmal](https://github.com/ClaraAkmal)

- [@Malak Waleed](https://github.com/MalakWaleed00)

