package org.app.liber.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPojo {

    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("u_fname")
    @Expose
    private String uFirstName;
    @SerializedName("u_lname")
    @Expose
    private String uLastName;
    @SerializedName("u_mobile")
    @Expose
    private String uMobile;
    @SerializedName("u_email")
    @Expose
    private String uEmail;
    @SerializedName("u_city")
    @Expose
    private String uCity;
    @SerializedName("u_address")
    @Expose
    private String uAddress;


    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuFirstName() {
        return uFirstName;
    }

    public void setuFirstName(String uFirstName) {
        this.uFirstName = uFirstName;
    }

    public String getuLastName() {
        return uLastName;
    }

    public void setuLastName(String uLastName) {
        this.uLastName = uLastName;
    }

    public String getuMobile() {
        return uMobile;
    }

    public void setuMobile(String uMobile) {
        this.uMobile = uMobile;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuCity() {
        return uCity;
    }

    public void setuCity(String uCity) {
        this.uCity = uCity;
    }

    public String getuAddress() {
        return uAddress;
    }

    public void setuAddress(String uAddress) {
        this.uAddress = uAddress;
    }
}
