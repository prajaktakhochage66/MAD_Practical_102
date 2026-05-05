package com.example.quickbite;

public class Product {
    private String id; // Changed to String for Firebase document ID
    private String name;
    private int quantity;
    private String mfgDate;
    private String expDate;

    public Product() {} // Required for Firebase

    public Product(String id, String name, int quantity, String mfgDate, String expDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.mfgDate = mfgDate;
        this.expDate = expDate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getMfgDate() { return mfgDate; }
    public String getExpDate() { return expDate; }
}
