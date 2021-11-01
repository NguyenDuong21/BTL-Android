package com.example.MyBTL.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private String Name,Category,Description,Image,tag;
    private int Prices;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
    public String gettag() {
        return tag;
    }

    public void settag(String tag2) {
        tag = tag2;
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

    public Product(String name, String category, String description, int prices, String tag2) {
        Name = name;
        Category = category;
        Description = description;
        Prices = prices;
        tag = tag2;
    }
}
