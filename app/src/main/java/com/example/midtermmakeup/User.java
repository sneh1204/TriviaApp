package com.example.midtermmakeup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String email, fname, lname, role, token;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", role='" + role + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public User(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("user_id");
        this.email = jsonObject.getString("user_email");
        this.fname = jsonObject.getString("user_fname");
        this.lname = jsonObject.getString("user_lname");
        this.role = jsonObject.getString("user_role");
        this.token = jsonObject.getString("token");
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
