package org.app.liber.pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserPojo implements Serializable
{

    @SerializedName("uname")
    @Expose
    private String uname;
    @SerializedName("uemail")
    @Expose
    private String uemail;
    @SerializedName("umob")
    @Expose
    private String umob;
    @SerializedName("upin")
    @Expose
    private String upin;
    @SerializedName("uaddress")
    @Expose
    private String uaddress;
    @SerializedName("usignup_date")
    @Expose
    private String usignupDate;
    @SerializedName("ulast_update")
    @Expose
    private String ulastUpdate;
    @SerializedName("udelete")
    @Expose
    private String udelete;
    @SerializedName("ucity")
    @Expose
    private String ucity;



    private final static long serialVersionUID = -616545887192840793L;

    public String getUcity() {
        return ucity;
    }

    public void setUcity(String ucity) {
        this.ucity = ucity;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUmob() {
        return umob;
    }

    public void setUmob(String umob) {
        this.umob = umob;
    }

    public String getUpin() {
        return upin;
    }

    public void setUpin(String upin) {
        this.upin = upin;
    }

    public String getUaddress() {
        return uaddress;
    }

    public void setUaddress(String uaddress) {
        this.uaddress = uaddress;
    }

    public String getUsignupDate() {
        return usignupDate;
    }

    public void setUsignupDate(String usignupDate) {
        this.usignupDate = usignupDate;
    }

    public String getUlastUpdate() {
        return ulastUpdate;
    }

    public void setUlastUpdate(String ulastUpdate) {
        this.ulastUpdate = ulastUpdate;
    }

    public String getUdelete() {
        return udelete;
    }

    public void setUdelete(String udelete) {
        this.udelete = udelete;
    }

//    @Override
//    public String toString() {
//        return new ToStringBuilder(this).append("uname", uname).append("uemail", uemail).append("umob", umob).append("upin", upin).append("uaddress", uaddress).append("usignupDate", usignupDate).append("ulastUpdate", ulastUpdate).append("udelete", udelete).toString();
//    }

}
