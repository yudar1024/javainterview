package com.roger.javainterview.concurrence;

public class VariableEntity {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "VariableEntity{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
