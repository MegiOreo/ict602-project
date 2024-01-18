package com.example.groupprojectict602;

public class Item {
    private String category;
    private String name;
    private String expiryDate;
    private String quantity;
    private String dateAdded;
    private String barcode; // New field for barcode

    // Empty constructor (required for Firebase)
    public Item() {
        // Default constructor required for calls to DataSnapshot.getValue(Item.class)
    }

    // Constructor with parameters including the new barcode field
    public Item(String category, String name, String expiryDate, String quantity, String dateAdded, String barcode) {
        this.category = category;
        this.name = name;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.dateAdded = dateAdded;
        this.barcode = barcode; // Initialize barcode
    }
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
        if (dateAdded != null && dateAdded.contains("timestamp:")) {
            return dateAdded.split(":")[1]; // Assuming the format is "timestamp:value"
        }
        return dateAdded; // Return the entire dateAdded if no special extraction is needed
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}

