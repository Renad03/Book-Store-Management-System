package com.example.bookstoregui.Model;

import com.example.bookstoregui.Model.Book;

public class HistoricalBook extends Book {
    public HistoricalBook(String title, String author, String stock, String price, String rating, int sales,String categorieType) {
        super ( title,  author,  stock,  price,  rating,  sales,"Historical");}
}
