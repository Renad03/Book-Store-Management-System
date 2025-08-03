/* package org.example.Database;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.example.Model.Categorie;

import java.util.ArrayList;
import java.util.List;

public class CategoryCRUD {
    private final MongoCollection<Document> collection;

    public CategoryCRUD() {
        Database database = Database.getInstance();
        this.collection = database.getCollection("category");
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        for (Document doc : collection.find()) {
            categories.add(Category.fromDocument(doc));
        }
        return categories;
    }

    public void createCategory(Category category) {
        collection.insertOne(category.toDocument());
    }

    public void deleteCategory(Category category) {
        collection.deleteOne(category.toDocument());
    }

    public void updateCategory(Category existingCategory, Category updatedCategory) {
        collection.updateOne(
                existingCategory.toDocument(),
                new Document("$set", updatedCategory.toDocument())
        );
    }

    // ... other CRUD operations as needed ...
}
*/
