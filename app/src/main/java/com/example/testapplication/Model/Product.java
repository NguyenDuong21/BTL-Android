package com.example.testapplication.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private String Name,Category,Description,Image;
    private int Prices;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Product()
    {

    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPrices() {
        return Prices;
    }

    public void setPrices(int prices) {
        Prices = prices;
    }

    public Product(String name, String category, String description, int prices) {
        Name = name;
        Category = category;
        Description = description;
        Prices = prices;
    }
}
