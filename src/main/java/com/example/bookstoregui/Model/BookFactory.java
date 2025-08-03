package com.example.bookstoregui.Model;
//import java.awt.*;

public class BookFactory {
    public  Book createBook(String bookType,String title, String author, String stock, String price, String rating,int sales) {
        switch (bookType) {
            case "Action":
                return new ActionBook(title, author,stock,price,  rating,sales,bookType);
            case "Historical":
                return new HistoricalBook(title,author,  stock,  price,  rating,sales,bookType);
            case "Classic" :
                return new Classic (title,author,  stock,  price,  rating,sales,bookType);
            case"Educational":
                return new Educational (title,author,  stock,  price,  rating,sales,bookType);
            default:
                throw new IllegalArgumentException("Unknown book type: " + bookType);
        }
    }
}