package com.example.framelibrary.database;


//该类是为了测试不使用反射进行数据库读取而写的 写在这里是不合理的，使用反射的话可以删除
@Deprecated
public class Person {

    private String name;
    private String address;
    private int age;

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
