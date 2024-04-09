package com.ucaldas.mssecurity.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class UserProfile {
    @Id
    private String _id;
    private String name;
    private  String last_name;
    private String city_of_residence;
    private String address;
    private String phone_number;
    @DBRef
    private User User_id;

    public UserProfile(String name, String phone_number, String address) {
        this.name = name;
        this.last_name = last_name;
        this.city_of_residence = city_of_residence;
        this.address = address;
        this.phone_number = phone_number;    
    }

    public UserProfile() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public void setphone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setCity_of_residence(String city_of_residence) {
        this.city_of_residence = city_of_residence;
    }

    public String getCity_of_residence() {
        return city_of_residence;
    }

    public User getUser_id() {
        return User_id;
    }

    public void setUser_id(User User_id) {
        this.User_id = User_id;
    }

}
