package org.app.liber.pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReview implements Serializable
{

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("ureview")
    @Expose
    private String ureview;
    @SerializedName("ustar")
    @Expose
    private String ustar;
    @SerializedName("udate")
    @Expose
    private String udate;
    @SerializedName("ubook")
    @Expose
    private String ubook;
    private final static long serialVersionUID = 2096026234566669026L;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUreview() {
        return ureview;
    }

    public void setUreview(String ureview) {
        this.ureview = ureview;
    }

    public String getUstar() {
        return ustar;
    }

    public void setUstar(String ustar) {
        this.ustar = ustar;
    }

    public String getUdate() {
        return udate;
    }

    public void setUdate(String udate) {
        this.udate = udate;
    }

    public String getUbook() {
        return ubook;
    }

    public void setUbook(String ubook) {
        this.ubook = ubook;
    }

    @Override
    public String toString() {
        //return new StringBuilder().append("uid"+uid).append("ureview", ureview).append("ustar", ustar).append("udate", udate).append("ubook", ubook).toString();
        return "";
    }

}
