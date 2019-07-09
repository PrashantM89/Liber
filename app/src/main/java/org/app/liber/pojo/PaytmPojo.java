package org.app.liber.pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaytmPojo implements Serializable
{

    @SerializedName("checksum")
    @Expose
    private String checksum;
    private final static long serialVersionUID = -7403278370313680591L;

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

//    @Override
//    public String toString() {
//        return new ToStringBuilder(this).append("checksum", checksum).toString();
//    }

}