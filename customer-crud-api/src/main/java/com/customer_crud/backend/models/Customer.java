package com.customer_crud.backend.models;

public class Customer {
    private Integer id;
    private String name;
    private String surname;
    private Integer age;

    public Customer(Customer customer){
        this.id = customer.id;
        this.name = customer.name;
        this.surname = customer.surname;
        this.age = customer.age;
    }

    public Customer() {
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }

    public Integer getAge() {
        return age;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Customer{id=" + id + ", name='" + name + "', surname='" + surname + "', age=" + age + "}";
    }
}
