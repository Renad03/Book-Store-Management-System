package com.example.bookstoregui.Model;

import com.example.bookstoregui.Database.AdminCRUD;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.example.bookstoregui.Database.UserCRUD;

import java.util.Scanner;


public class User {
    String id;
    String username;
    String password;
    String address;
    String phoneNumber;
    String email;
    static UserCRUD userCRUD = new UserCRUD ();
    static User us = null;

    static Scanner scanner = new Scanner (System.in);

    // Constructors, getters, and setters
    public User(String id,String username, String password, String phoneNumber, String address, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
    }

    public User(String username, String password, String address, String phoneNumber, String email) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public User(User other) {
        if (other != null) {
            this.username = other.username;
            this.password = other.password;
            this.phoneNumber = other.phoneNumber;
            this.address = other.address;
            this.email = other.email;
        }
    }

    public User() {
        // No-argument constructor
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'';
    }

    public Document toDocument() {
        return new Document
                ("username", getUsername())
                .append("password", getPassword())
                .append("phoneNumber", getPhoneNumber())
                .append("address", getAddress())
                .append("email", getEmail());
    }

    public static User fromDocument(Document doc) {
        String id = doc.getObjectId("_id").toHexString();
        User user2 = new User (id,
                doc.getString ("username"),
                doc.getString ("password"),
                doc.getString ("phoneNumber"),
                doc.getString ("address"),
                doc.getString ("email"));
        return user2;
    }

    public static User get_user(String enteredUsername) {
        User userr = null;
        AdminCRUD adminCRUD = new AdminCRUD();
        if(enteredUsername.equals("Clara")){
            userr = adminCRUD.getAdminByUsername("Clara");
        }
        else{
            userr = userCRUD.getUserByUsername (enteredUsername);
        }
        return userr;
    }

    public static User login(String Username, String Password) {

        us = User.get_user (Username);
        if (us.getPassword().equals(Password)){
            return us;
        }
        else {
            return null;
        }

    }

    static public User signup(String username, String password, String phoneNumber, String email, String address) {
        User user = new User(username,password,phoneNumber,address,email);
        userCRUD.createUser(user);
        return user;
    }

    public void update_details(String choice) {
        // Assuming filterUser is a User object
        User filterUser = get_user (us.getUsername ());
        switch (choice) {
            case "Name":
                System.out.println ("Enter new Name: ");
                String newName = scanner.nextLine ();
                filterUser.setUsername (newName);  // Update the user object
                break;

            case "Email":
                System.out.println ("Enter new Email: ");
                String newEmail = scanner.nextLine ();
                filterUser.setEmail (newEmail);  // Update the user object
                break;

            case "Password":
                System.out.println ("Enter new Password: ");
                String newPassword = scanner.nextLine ();
                filterUser.setPassword (newPassword);  // Update the user object
                break;

            case "Address":
                System.out.println ("Enter new Address: ");
                String newAddress = scanner.nextLine ();
                filterUser.setAddress (newAddress);  // Update the user object
                break;

            case "Phone":
                System.out.println ("Enter new Phone: ");
                String newPhone = scanner.nextLine ();
                filterUser.setPhoneNumber (newPhone);  // Update the user object
                break;

            default:
                System.out.println ("Invalid option.");
                return;
        }


        userCRUD.updateUser (us, filterUser);

    }
}


