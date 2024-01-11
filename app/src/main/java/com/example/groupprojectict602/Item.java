package com.example.groupprojectict602;

public class Item {
    //private String id;
    private String category;
    private String name;
    private String expiryDate;
    private String quantity;
    private String dateAdded;

    // Empty constructor (required for Firebase)
    public Item() {
        // Default constructor required for calls to DataSnapshot.getValue(Item.class)
    }

    // Constructor with parameters
    public Item(String id, String category, String name, String expiryDate, String quantity, String dateAdded) {
        //this.id = id;
        this.category = category;
        this.name = name;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.dateAdded = dateAdded;
    }

    // Getter for id
//    public String getId() {
//        return id;
//    }

    // Setter for id
//    public void setId(String id) {
//        this.id = id;
//    }

    // Getter for category
    public String getCategory() {
        return category;
    }

    // Setter for category
    public void setCategory(String category) {
        this.category = category;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for expireddate
    public String getExpiryDate() {
        return expiryDate;
    }

    // Setter for expireddate
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    // Getter for quantity
    public String getQuantity() {
        return quantity;
    }

    // Setter for quantity
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    // Getter for dateadded
    public String getDateAdded() {
        // Extract and return the timestamp from the dateAdded field
        // For example, if dateAdded is in the format "timestamp:someValue", extract the timestamp part
        // Return the timestamp or the entire dateAdded depending on your requirements
        // This is a simplified example; you might need to adjust it based on your actual data structure
        if (dateAdded != null && dateAdded.contains("timestamp:")) {
            return dateAdded.split(":")[1]; // Assuming the format is "timestamp:value"
        }
        return dateAdded; // Return the entire dateAdded if no special extraction is needed
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}

