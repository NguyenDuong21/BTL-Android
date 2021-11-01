package com.example.MyBTL.Model;

import java.io.Serializable;

public class ProductCart implements Serializable {
    private String Name,Category,Description,Image,tag;
    private int Prices, Amount;

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

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

    public ProductCart()
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

    public ProductCart(String name, String category, String description, int prices, String tag2, int amount, String image) {
        Name = name;
        Category = category;
        Description = description;
        Prices = prices;
        tag = tag2;
        Amount = amount;
        Image = image;
    }
}
