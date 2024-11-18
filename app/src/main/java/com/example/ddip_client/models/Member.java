package com.example.ddip_client.models;

public class Member {
    private String id;
    private String password;
    private String name;
    private String email;
    private String contact_number;
    private String userType;

    public Member() {}

    public Member(String userid, String userpwd, String username, String email, String admin, String contact_number) {
        this.id = userid;
        this.password = userpwd;
        this.name = username;
        this.email = email;
        this.userType = admin;
        this.contact_number = contact_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_number(){
        return contact_number;
    }

    public void setContact_number(String contact_number){
        this.contact_number = contact_number;
    }

    public String getUser_type() {
        return userType;
    }

    public void setUser_type(String user_type) {
        this.userType = user_type;
    }

    public void setAllData(String id, String name, String password, String email, String contactNumber, String userType) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.contact_number = contactNumber;
        this.userType = userType;
    }
}
