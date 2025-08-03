package com.example.bookstoregui.Model;
import org.bson.Document;
import com.example.bookstoregui.Database.BookCRUD;
import com.example.bookstoregui.Database.OrderCRUD;
import com.example.bookstoregui.Database.UserCRUD;

import java.util.*;

public class Admin extends User {
    List<Book> bookInventory = new ArrayList<> ();
    BookCRUD crud = new BookCRUD ();
    OrderCRUD orderCRUD = new OrderCRUD ();
    UserCRUD userCRUD = new UserCRUD ();
    Scanner scanner = new Scanner (System.in);

    public Admin(String username, String password, String phoneNumber, String address, String email) {
        super (username, password, phoneNumber, address, email);
    }

    public String toString() {
        return "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'';
    }

    public Document toDocument() {
        return new Document ("id", id)
                .append ("username", username)
                .append ("password", password)
                .append ("phoneNumber", phoneNumber)
                .append ("address", address)
                .append ("email", email);
    }

    public static Admin fromDocument(Document doc) {
        Admin admin = new Admin (doc.getString ("username"),
                doc.getString ("password"),
                doc.getString ("phoneNumber"),
                doc.getString ("address"),
                doc.getString ("email"));
        return admin;
    }


    public void addBook(String bookName, String author, String stock, String price, String rating, String categorie) {
        BookFactory bf = new BookFactory ();
        Book b1 = bf.createBook (categorie, bookName, author, stock, price, rating, 0);
        crud.createBook (b1);
        System.out.println ("Book added successfully");
    }

    public void deleteBook(String bookName) {
        Book book = crud.getBookByTitle (bookName);
        if (book != null) {
            crud.deleteBook (book);
            System.out.println ("Book deleted successfully");
        } else {
            System.out.println ("Book not found.");
        }
    }

    public void editBook(String bookName, String newTitle, String newAuthor, String newStock, String newPrice, String newRating, String newCategory, int newSales) {
        Book book = crud.getBookByTitle (bookName);
        if (book != null) {
            System.out.println ("Editing book: " + bookName);
            System.out.println ("Current details - Author: " + book.getAuthor () + ", Stock: " + book.getStock () + ", Price: " + book.getPrice () + ", Rating: " + book.getRating () + ", Category: " + book.getCategorieType () + ", Sales: " + book.getSales ());

            Book newBook = book.get_clone ();
            newBook.setTitle (newTitle);
            newBook.setAuthor (newAuthor);
            newBook.setStock (newStock);
            newBook.setPrice (newPrice);
            newBook.setRating (newRating);
            newBook.setCategorieType (newCategory);
            newBook.setSales (newSales);

            crud.updateBook (book, newBook);
            System.out.println ("Book edited successfully");
        } else {
            System.out.println ("Book not found.");
        }
    }



    public void updateBookAvailability(String bookName, String newStock) {
        Book book = crud.getBookByTitle (bookName);
        if (book != null) {
            System.out.println ("Current stock for " + bookName + ": " + book.getStock ());
            if (book.getStock ().equals ("0")) {
                System.out.println ("Not available");
            } else {
                System.out.println ("Available");
            }

            System.out.println ("Do you want to change the stock? ('y' for yes / 'n' for no): ");
            if (newStock.equals ("y")) {
                System.out.println ("Enter new stock value: ");
                Book newBook = new Book (book.getTitle (), book.getAuthor (), newStock, book.getPrice (), book.getRating (), book.getSales (), book.getCategorieType ());
                crud.updateBook (book, newBook);
                System.out.println ("Stock updated successfully");
            }
        } else {
            System.out.println ("Book not found.");
        }
    }
    public List<Book> trackStock() {
        bookInventory = crud.getAllBooks ();
        return bookInventory;
    }
    public List<Book> topSelling() {
        bookInventory = crud.getAllBooks ();
        List<Book> top_books=new ArrayList<> ();
        for (int i = 1; i <= 3; i++) {
            int max = 0;
            int index = -1;
            for (int j = 0; j < bookInventory.size (); j++) {
                Book b = bookInventory.get (j);
                if (b.getSales () > max) {
                    max = b.getSales ();
                    index = j;
                }
            }
            if (index != -1) {
                Book topBook = bookInventory.get (index);
                top_books.add (topBook);
                bookInventory.remove (index);
            }
        }return top_books;

    }

    public  ArrayList<AbstractMap.SimpleEntry<String, Integer>> getPopularCategories() {
        List<Book> categories;
        List<Book> bookInventory = crud.getAllBooks();
        ArrayList<AbstractMap.SimpleEntry<String, Integer>> list = new ArrayList<>();

        for (Book b : bookInventory) {
            categories = crud.getBooksByCategory(b.getCategorieType());
            String category = b.getCategorieType();
            int quantity = categories.size();
            list.add(new AbstractMap.SimpleEntry<>(category, quantity));
        }

        // Use a LinkedHashSet to remove duplicates and preserve order
        LinkedHashSet<AbstractMap.SimpleEntry<String, Integer>> set = new LinkedHashSet<>(list);

        // Convert back to an ArrayList
        list = new ArrayList<>(set);

        // Sort the list by quantity in descending order
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        // Prepare the result list with top 3 categories
        ArrayList<AbstractMap.SimpleEntry<String, Integer>> topCategories = new ArrayList<>();
        for (int i = 0; i < Math.min(3, list.size()); i++) {
            topCategories.add(list.get(i));
        }

        return topCategories;
    }


    public List<Order> pending()
    {
        List<Order> Pending_order = new ArrayList<> ();
        List<Order> orders = orderCRUD.getAllOrders ();
        for (Order o : orders){
            if(o.getStatus ().equals ("Pending")){
                Pending_order.add (o) ;
            }

        }
        return Pending_order;
    }
    public List<Order> Approved()
    {
        List<Order> Confirm_order = new ArrayList<> ();
        List<Order> orders = orderCRUD.getAllOrders ();
        for (Order o : orders){
            if(o.getStatus ().equals ("Approved")){
                Confirm_order.add (o) ;
            }

        }
        return  Confirm_order;
    }
    public void confirmOrCancelOrders(String choice,Order order) {
        switch (choice) {
            case "Approve":
                orderCRUD.updateOrder (order, "Approved");
                break;
            case "Cancel":
                orderCRUD.updateOrder (order, "Canceled");
                break;
            default:
                System.out.println ("Invalid choice");
                break;
        }

    }
    public void Delivered_Orders(Order order)
    {
        orderCRUD.updateOrder (order, "Delivered");
    }
    public  List<Order> viewOrders() {
        List<Order> orders = orderCRUD.getAllOrders ();
        return orders;
    }
}
//    public void addBook(){
//        System.out.println("Book name: ");
//        String bookName = scanner.nextLine();
//        System.out.println("Book author: ");
//        String author= scanner.nextLine();
//        System.out.println("Book stock: ");
//        String stock= scanner.nextLine();
//        System.out.println("Book price: ");
//        String price= scanner.nextLine();
//        System.out.println("Book rating: ");
//        String rating= scanner.nextLine();
//        System.out.println("Book categorie: ");
//        String categorie= scanner.nextLine();
//        BookFactory bf=new BookFactory();
//        Book b1= bf.createBook (bookName,author,stock,price,rating,categorie,0);
//        crud.createBook (b1);
//        System.out.println("Book added successfully ");
//    }
//
//    public void deleteBook(){
//        System.out.println("Book name: ");
//        String bookName = scanner.nextLine();
//        Book book=crud.getBookByTitle(bookName);
//        crud.deleteBook(book);
//        System.out.println("Book deleted successfully");
//    }
//    public void editBook(){
//        System.out.println("Book name that you want to edit: ");
//        String bookName = scanner.nextLine();
//        Book book=crud.getBookByTitle(bookName);
//        System.out.println("Book author: ");
//        System.out.println(book.getAuthor());
//        System.out.println("Book stock: ");
//        System.out.println(book.getStock());
//        System.out.println("Book price: ");
//        System.out.println(book.getPrice());
//        System.out.println("Book rating: ");
//        System.out.println(book.getRating());
//        System.out.println("Book categorie: ");
//        System.out.println(book.getCategorieType ());
//        System.out.println("Book sales: ");
//        System.out.println(book.getSales());
//
//        Book Newbook =book.get_clone();
//
//        String c;
//        do {
//            System.out.println("1)Edit name"+" 2)Edit author"+" 3)Edit stock"+" 4)Edit price"+" 5)Edit rating"+" 6)Edit categorie");
//            int choice = scanner.nextInt();
//            scanner.nextLine();
//            switch (choice) {
//                case 1:
//                    System.out.println("Name: ");
//                    String name = scanner.nextLine();
//                    Newbook.setTitle(name);
//                    break;
//                case 2:
//                    System.out.println("author: ");
//                    String author = scanner.nextLine();
//                    Newbook.setAuthor(author);
//                    break;
//                case 3:
//                    System.out.println("stock: ");
//                    String stock = scanner.nextLine();
//                    Newbook.setStock(stock);
//                    break;
//                case 4:
//                    System.out.println("price: ");
//                    String price = scanner.nextLine();
//                    Newbook.setPrice(price);
//                    break;
//                case 5:
//                    System.out.println("rating: ");
//                    String rating = scanner.nextLine();
//                    Newbook.setRating(rating);
//                    break;
//                case 6:
//                    System.out.println("categorie: ");
//                    String  categorie = scanner.nextLine();
//                    Newbook.setCategorieType(categorie);
//                    break;
//                case 7:
//                    System.out.println("sales: ");
//                    int  sales = scanner.nextInt();
//                    Newbook.setSales(sales);
//                    scanner.nextLine();
//                    break;
//                default:
//                    System.out.println ("Invalid option.");
//                    return;
//            }
//            System.out.println("Do you want to edit any thing else ?('y' for yes and 'n' for no ");
//            c = scanner.nextLine();
//        }while(c.equals("y"));
//
//        crud.updateBook(book,Newbook);
//    }
//    public void trackStock(){
//        bookInventory=crud.getAllBooks();
//        for (Book doc : bookInventory) {
//            System.out.println(doc.getTitle()+" "+doc.getStock());
//        }
//    }
//    public void update_book_availability(){
//        System.out.println("Book name: ");
//        String bookName = scanner.nextLine();
//        Book book=crud.getBookByTitle(bookName);
//        System.out.println(book.getStock());
//        if(book.getStock().equals("0")){
//            System.out.println("not available");
//        }
//        else{
//            System.out.println("Available");
//        }
//        System.out.println("Do you want to change number of book in the stock ? ('y' for yes / 'n' for no ");
//        String availability;
//        availability = scanner.nextLine();
//        if (availability.equals("y")){
//            System.out.println("Stock : ");
//            String Stock = scanner.nextLine();
//            Book newBook=new Book(book.getTitle(),book.getAuthor(),Stock,book.getPrice(),book.getRating(),book.getSales(),book.getCategorieType());
//            crud.updateBook(book,newBook);
//        }
//    }
//
//    public void Top_selling(){
//        bookInventory=crud.getAllBooks();
//        String title=null;
//        int f=0;
//
//        for(int i=1;i<=3;i++) {
//            int max=0;
//            int j=0;
//            for (Book b : bookInventory) {
//                if (b.getSales() > max) {
//                    max = b.getSales();
//                    title = b.getTitle();
//                    f=j;
//                }
//                j++;
//            }
//
//            System.out.println(title + " " + max);
//          bookInventory.remove(f);// f = index of the book that has maximum sales
//
//        }
//    }
//    public void Popular_Categories(){
//        List<Book>categories=new ArrayList<>();
//        bookInventory=crud.getAllBooks();
//        ArrayList<AbstractMap.SimpleEntry<String, Integer>> list = new ArrayList<>();
//            for (Book b : bookInventory) {
//               categories=crud.getBooksByCategory(b.getCategorieType());
//               String c=b.getCategorieType();
//               int quantity=categories.size();
//               list.add(new AbstractMap.SimpleEntry<>(c,quantity));
//            }
//        // Use a LinkedHashSet to remove duplicates and preserve order
//        LinkedHashSet<AbstractMap.SimpleEntry<String, Integer>> set = new LinkedHashSet<>(list);
//
//        // Convert back to an ArrayList
//        list = new ArrayList<>(set);
//
//            list.sort((categorie_name, quantity) -> quantity.getValue().compareTo(categorie_name.getValue()));
//            for(int i=0;i<3;i++){
//                System.out.println(list.get(i).getKey());
//            }
//    }
//    public void confirm_cancel_orders(){
//        List<Order>orders= orderCRUD.getAllOrders();
//        User u=new User();
//        for (Order o:orders) {
//            if(o.getStatus().equals("Pending")) {
//                u = userCRUD.get_user_by_id(o.getUserID());
//                if (u != null) {
//
//                    System.out.println("Username :" + u.getUsername() + " " + "Order :" + o.getBooks() + " " + " total price :" + o.getTotalPrice());
//                    System.out.println("Do you want to confirm or cancel this order (Press 1 to confirm or 2 for cancel )");
//                    int choice = scanner.nextInt();
//
//                    switch (choice) {
//                        case 1:
//                            orderCRUD.updateOrder(o,"Approved");
//
//                            break;
//                        case 2:
//                            orderCRUD.updateOrder(o,"Canceled");
//                            break;
//                        default:
//                            System.out.println("invalid choice");
//                            break;
//                    }
//                    scanner.nextLine();
//                } else {
//                    System.out.println(" status :" + o.getStatus() + " user id :" + o.getUserID() + "items : " + o.getBooks() + "price: " + o.getTotalPrice());
//                    System.out.println("this order does not contain user id do you want to cancel it ?(press'y' for yes or 'n' for no)");
//                    String choice = scanner.nextLine();
//                    if (choice.equals("y")) {
//                        orderCRUD.updateOrder(o,"Canceled");
//                    }
//                }
//            }
//        }
//    }
//    public void view_orders(){
//        List<Order>orders= orderCRUD.getAllOrders();
//        for (Order o:orders) {
//            System.out.println("Order id: "+ o.getId() +" status :"+o.getStatus()+" user id :"+o.getUserID()+"  items : "+o.getBooks()+"  price: "+o.getTotalPrice());
//        }
//    }
//}