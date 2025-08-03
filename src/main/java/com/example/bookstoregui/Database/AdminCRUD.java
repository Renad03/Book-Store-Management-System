package com.example.bookstoregui.Database;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.example.bookstoregui.Model.Admin;

import java.util.ArrayList;
import java.util.List;

public class AdminCRUD {
    private final MongoCollection<Document> collection;

    public AdminCRUD() {
        Database database = Database.getInstance();
        this.collection = database.getCollection("admin");
    }

    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        for (Document doc : collection.find()) {
            admins.add(Admin.fromDocument(doc));

        }
        return admins;
    }

    public void createAdmin(Admin admin) {
        collection.insertOne(admin.toDocument());
    }

    public void deleteAdmin(Admin admin) {
        collection.deleteOne(admin.toDocument());
    }

    public void updateAdmin(Admin filterAdmin, Admin updateAdmin) {
        collection.updateOne(
                filterAdmin.toDocument(),
                new Document("$set", updateAdmin.toDocument())
        );
    }

    public Admin getAdminByUsername(String username) {
        Document filter = new Document("username", username);
        return getAdminByFilter(filter);
    }

    private Admin getAdminByFilter(Document filter) {
        Document doc = collection.find(filter).first();
        return doc != null ? Admin.fromDocument(doc) : null;
    }
}