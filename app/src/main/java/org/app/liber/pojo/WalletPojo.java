package org.app.liber.pojo;


import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
//import org.apache.commons.lang.builder.ToStringBuilder;

public class WalletPojo implements Serializable
{

    @SerializedName("wuid")
    @Expose
    private String wuid;
    @SerializedName("wmob")
    @Expose
    private String wmob;
    @SerializedName("wamntAdded")
    @Expose
    private String wamntAdded;
    @SerializedName("wbookDate")
    @Expose
    private String wbookDate;
    @SerializedName("wbookName")
    @Expose
    private String wbookName;
    @SerializedName("wdelete")
    @Expose
    private String wdelete;
    private final static long serialVersionUID = 5376158001831910319L;

    public String getWuid() {
        return wuid;
    }

    public void setWuid(String wuid) {
        this.wuid = wuid;
    }

    public String getWmob() {
        return wmob;
    }

    public void setWmob(String wmob) {
        this.wmob = wmob;
    }

    public String getWamntAdded() {
        return wamntAdded;
    }

    public void setWamntAdded(String wamntAdded) {
        this.wamntAdded = wamntAdded;
    }

    public String getWbookDate() {
        return wbookDate;
    }

    public void setWbookDate(String wbookDate) {
        this.wbookDate = wbookDate;
    }

    public String getWbookName() {
        return wbookName;
    }

    public void setWbookName(String wbookName) {
        this.wbookName = wbookName;
    }

    public String getWdelete() {
        return wdelete;
    }

    public void setWdelete(String wdelete) {
        this.wdelete = wdelete;
    }

//    @Override
//    public String toString() {
//        return new ToStringBuilder(this).append("wuid", wuid).append("wmob", wmob).append("wamntAdded", wamntAdded).append("wbookDate", wbookDate).append("wbookName", wbookName).append("wdelete", wdelete).toString();
//    }

}
