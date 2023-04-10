package com.example.MyBTL.Model;

public class Category {
    String Name,Image, Tag;

    public Category()
    {

    }

    public Category(String name, String image, String tag) {
        Name = name;
        Image = image;
        Tag = tag;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    @Override
    public String toString() {
        return Name;
    }
}
