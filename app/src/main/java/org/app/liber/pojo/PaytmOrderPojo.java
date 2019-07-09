package org.app.liber.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaytmOrderPojo implements Serializable {
    @SerializedName("checksum")
    @Expose
    private String checksum;
    @SerializedName("mid")
    @Expose
    private Object mid;
    @SerializedName("order_ID")
    @Expose
    private Object orderID;
    @SerializedName("cust_ID")
    @Expose
    private Object custID;
    @SerializedName("industry_TYPE_ID")
    @Expose
    private Object industryTYPEID;
    @SerializedName("channel_ID")
    @Expose
    private Object channelID;
    @SerializedName("txn_AMOUNT")
    @Expose
    private Object txnAMOUNT;
    @SerializedName("website")
    @Expose
    private Object website;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("mobile_NO")
    @Expose
    private Object mobileNO;
    @SerializedName("callback")
    @Expose
    private Object callback;
    private final static long serialVersionUID = -2682835824748905572L;

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public Object getMid() {
        return mid;
    }

    public void setMid(Object mid) {
        this.mid = mid;
    }

    public Object getOrderID() {
        return orderID;
    }

    public void setOrderID(Object orderID) {
        this.orderID = orderID;
    }

    public Object getCustID() {
        return custID;
    }

    public void setCustID(Object custID) {
        this.custID = custID;
    }

    public Object getIndustryTYPEID() {
        return industryTYPEID;
    }

    public void setIndustryTYPEID(Object industryTYPEID) {
        this.industryTYPEID = industryTYPEID;
    }

    public Object getChannelID() {
        return channelID;
    }

    public void setChannelID(Object channelID) {
        this.channelID = channelID;
    }

    public Object getTxnAMOUNT() {
        return txnAMOUNT;
    }

    public void setTxnAMOUNT(Object txnAMOUNT) {
        this.txnAMOUNT = txnAMOUNT;
    }

    public Object getWebsite() {
        return website;
    }

    public void setWebsite(Object website) {
        this.website = website;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getMobileNO() {
        return mobileNO;
    }

    public void setMobileNO(Object mobileNO) {
        this.mobileNO = mobileNO;
    }

    public Object getCallback() {
        return callback;
    }

    public void setCallback(Object callback) {
        this.callback = callback;
    }

}
