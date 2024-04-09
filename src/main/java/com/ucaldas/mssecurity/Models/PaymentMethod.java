package com.ucaldas.mssecurity.Models;

public class PaymentMethod {
    private String _id;
    private String name;
    private String type;
    private  int status;

    @DBRef
    private User User_id;

    public PaymentMethod(String _id, String name, String type,int status) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public User getUser_id() {
        return User_id;
    }

    public void setUser_id(User user_id) {
        User_id = user_id;
    }
}
