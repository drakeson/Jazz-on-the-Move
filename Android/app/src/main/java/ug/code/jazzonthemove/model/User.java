package ug.code.jazzonthemove.model;

public class User {
    private String id;
    private String username;
    private String imageurl;
    private String bio;
    private String balance;
    private String phone;
    private String email;
    private String profession;
    private String sms;
    private String status;
    private String search;
    private String invested;
    private String earned;

    public User(String id, String username, String imageurl, String bio, String balance, String phone, String email, String profession, String sms, String status, String search, String invested, String earned) {
        this.id = id;
        this.username = username;
        this.imageurl = imageurl;
        this.bio = bio;
        this.balance = balance;
        this.phone = phone;
        this.email = email;
        this.profession = profession;
        this.sms = sms;
        this.status = status;
        this.search = search;
        this.invested = invested;
        this.earned = earned;
    }

    public User() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }


    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getInvested() {
        return invested;
    }

    public void setInvested(String invested) {
        this.invested = invested;
    }

    public String getEarned() {
        return earned;
    }

    public void setEarned(String earned) {
        this.earned = earned;
    }
}
