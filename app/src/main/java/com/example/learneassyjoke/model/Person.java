package com.example.learneassyjoke.model;

public class Person {

    private String name;
    private String address;
    private int age;

    public Person() {

    }

    public Person(String name, String address, int age) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}
