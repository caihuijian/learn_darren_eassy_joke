package com.example.testrecyclerview;

/**
 * Created by hjcai on 2021/5/13.
 */
public class User {
    public User(String userName, int age, String address) {
        this.userName = userName;
        this.age = age;
        this.imageUrl = address;
    }

    String userName;
    int age;
    String imageUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
