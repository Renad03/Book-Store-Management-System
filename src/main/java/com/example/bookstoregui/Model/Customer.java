package com.example.bookstoregui.Model;

import com.example.bookstoregui.Database.UserCRUD;
import org.bson.Document;
import com.example.bookstoregui.Database.BookCRUD;
import com.example.bookstoregui.Database.OrderCRUD;
import com.example.bookstoregui.Database.CartCRUD;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Customer extends User {
    List<Book> books = new ArrayList<> ();
    ArrayList<String> orderHistory = new ArrayList<> ();
    ArrayList<String> Cart = new ArrayList<> ();
    static Scanner scanner = new Scanner (System.in);
    Scanner scanner1 = new Scanner (System.in);
    BookCRUD b = new BookCRUD ();
    Book book;
    CartCRUD cartcrud = new CartCRUD();
    Cart cart;
    OrderCRUD orderCRUD = new OrderCRUD();
    static UserCRUD userCRUD = new UserCRUD();
    static Customer us = null;
    public Customer(String username, String password, String phoneNumber, String address, String email) {
        super (username, password, phoneNumber, address, email);
    }

    public Customer() {
    }

    public void retrievefromDb() {
        for (Book book : books) {
            System.out.println ("Book Details: " +
                    "Title: " + book.getTitle () + ", " +
                    "Author: " + book.getAuthor () + ", " +
                    "Stock: " + book.getStock () + ", " +
                    "Price: " + book.getPrice () + ", " +
                    "Rating: " + book.getRating () + ", " +
                    "Sales: " + book.getSales () + ", " +
                    "Category: " + book.getCategorieType ());

        }
    }

    public void Browse() {
        books = b.getAllBooks ();
        retrievefromDb ();
    }

    // Method to search books by title
    public void searchBooks() {
        System.out.println ("Enter 1-> Title 2-> Author:");

        int choice = scanner.nextInt ();
        scanner.nextLine (); // This line consumes the leftover newline character

        switch (choice) {
            case 1:
                System.out.println ("Enter the book name");
                String bookName = scanner.nextLine (); // Now this will work properly
                book = b.getBookByTitle (bookName);
                if (book != null) {
                    System.out.println ("Book Details: " +
                            "Title: " + book.getTitle () + ", " +
                            "Author: " + book.getAuthor () + ", " +
                            "Stock: " + book.getStock () + ", " +
                            "Price: " + book.getPrice () + ", " +
                            "Rating: " + book.getRating () + ", " +
                            "Sales: " + book.getSales () + ", " +
                            "Category: " + book.getCategorieType ());
                } else {
                    System.out.println ("Book Not Found");
                }
                break;

            case 2:
                System.out.println ("Enter the Author");
                String author = scanner.nextLine (); // Use nextLine() for author
                books = b.getBooksByAuthor (author);
                retrievefromDb();

        }
    }

    public void avaliblty(){
        books=b.getAllBooks ();
        for (Book book : books) {
            int stock = Integer.parseInt (book.getStock ());
            if (stock > 0) {
                System.out.println ("Book Details: " +
                        "Title: " + book.getTitle () + ", " +
                        "Author: " + book.getAuthor () + ", " +
                        "Price: " + book.getPrice () + ", " +
                        "Rating: " + book.getRating () + ", " +
                        "Sales: " + book.getSales () + ", " +
                        "Category: " + book.getCategorieType ());
            }
        }
    }
    public void filterBooks_Categories() {
        System.out.println ("Enter the Categorie you want :");
        String categ = scanner.nextLine ();
        books = b.getBooksByCategory (categ);
        retrievefromDb ();
    }

    private void display_Book(Book book) {
        System.out.println ("Books: " +
                "Title: " + book.getTitle () + ", " +
                "Author: " + book.getAuthor () + ", " +
                "Stock: " + book.getStock () + ", " +
                "Price: " + book.getPrice () + ", " +
                "Rating: " + book.getRating () + ", " +
                "Sales: " + book.getSales () + ", " +
                "Category: " + book.getCategorieType ());
    }

    public void sort() {
        System.out.println ("1 -> Sort by price  :");
        System.out.println ("2 -> Sort by popularity :");
        int choice = scanner.nextInt ();

        switch (choice) {
            case 1:
                System.out.println ("Enter the minimum price you want to filter with:");
                int price = scanner.nextInt ();
                books = b.getAllBooks ();

                // Sort by price in descending order
                books.sort ((book1, book2) -> Integer.compare (Integer.parseInt (book2.getPrice ()), Integer.parseInt (book1.getPrice ())));


                for (Book book : books) {
                    int bookPrice = Integer.parseInt (book.getPrice ());
                    if (bookPrice > price) {
                        display_Book (book);
                    }
                }
                break;

            case 2:
                books = b.getAllBooks ();

                books.sort ((book1, book2) -> Integer.compare (book2.getSales (), book1.getSales ()));

                for (Book book : books) {
                    if (book.getSales () > 30) {
                        display_Book (book);
                    }
                }
                break;

            default:
                System.out.println ("Invalid choice. Please try again.");
                break;
        }
    }

    public void add_cart(String userId)
    {
        Browse();
        System.out.println ("Enter the name of the book you want to add to cart");
        String choose_book = scanner1.nextLine ();
        book = b.getBookByTitle(choose_book);
        cart=cartcrud.getCartByUserId(userId);
        if(book.getStock () != "0")
        {
            cartcrud.addBookToCart(choose_book,cart);
        }
        else
        {
            System.out.println ("sorry th book out of stock :((");
        }
    }

    public static Customer Customerlogin() {
        System.out.println ("enter your name");
        String Username = scanner.next ();

        us = userCRUD.getUserByUsername(Username);
        if (us != null) {
            System.out.println ("Login successful!");
        } else {
            System.out.println ("Invalid username or password.");
        }
        return us;
    }

    public void addReview(String userId){
        Browse();
        System.out.println ("Enter the name of the book you want to add to cart");
        String choose_book = scanner1.nextLine ();
        System.out.println ("Enter your review");
        String review = scanner1.nextLine ();
        book = b.getBookByTitle(choose_book);
        userCRUD.addReview(book.getTitle(), userId, review);
    }

    public void createOrder(Cart cart){
        double sum = 0.0;
        List<String>bookTitles = new ArrayList<>();
        List<Book> books = cartcrud.getBooksInCart(cart.getId());
        for (Book book: books) {
            bookTitles.add(book.getTitle());
            sum += Double.parseDouble(book.getPrice());
        }
        BigDecimal totalPrice =  BigDecimal.valueOf(sum);
        Order order = new Order(bookTitles, totalPrice, cart.getUserID());
        orderCRUD.createOrder(order);
    }
}

