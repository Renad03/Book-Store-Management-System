package com.example.bookstoregui.Database;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.example.bookstoregui.Model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookCRUD {
    private final MongoCollection<Document> collection;

    public BookCRUD() {
        Database database = Database.getInstance (); // Assuming a Database class exists to manage connection
        this.collection = database.getCollection ("book");
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<> ();
        for (Document doc : collection.find ()) {
            books.add (Book.fromDocument (doc));
        }
        return books;
    }

    public void createBook(Book book) {
        collection.insertOne (book.toDocument ());
    }

    public void deleteBook(Book book) {
        collection.deleteOne (book.toDocument ());
    }

    public void updateBook(Book existingBook, Book updatedBook) {
        collection.updateOne (
                existingBook.toDocument (),
                new Document ("$set", updatedBook.toDocument ())
        );
    }

    public Book getBookByTitle(String title) {
        Document filter = new Document ("title", title);
        return getBookByFilter (filter);
    }

    public List<Book> getBooksByCategory(String category) {
        List<Book> books = new ArrayList<> ();


        Document filter = new Document ("categorie_Type", category);


        for (Document doc : collection.find (filter)) {
            books.add (Book.fromDocument (doc));
        }

        return books;
    }

    public List<Book> getBooksByAuthor(String author) {
        List<Book> books = new ArrayList<> ();


        Document filter = new Document ("author", author);


        for (Document doc : collection.find (filter)) {
            books.add (Book.fromDocument (doc));
        }

        return books;
    }

    public Book getBookByFilter(Document filter) {
        Document doc = collection.find (filter).first ();
        return doc != null ? Book.fromDocument (doc) : null;
    }
}