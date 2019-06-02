package org.app.liber.pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionPojo implements Serializable
{

@SerializedName("txDeliveryDate")
@Expose
private String txDeliveryDate;
@SerializedName("txId")
@Expose
private String txId;
@SerializedName("txDate")
@Expose
private String txDate;
@SerializedName("txDeliverySts")
@Expose
private String txDeliverySts;
@SerializedName("txPaymentMode")
@Expose
private String txPaymentMode;
@SerializedName("txUser")
@Expose
private String txUser;
@SerializedName("txDelete")
@Expose
private String txDelete;
@SerializedName("txType")
@Expose
private String txType;
@SerializedName("txMob")
@Expose
private String txMob;
@SerializedName("txAmount")
@Expose
private String txAmount;
@SerializedName("txReturnDate")
@Expose
private String txReturnDate;
@SerializedName("txBook")
@Expose
private String txBook;

private final static long serialVersionUID = -7690592283167903917L;

    public String getTxMob() {
        return txMob;
    }

    public void setTxMob(String txMob) {
        this.txMob = txMob;
    }


    public String getTxBook() {
        return txBook;
    }

    public void setTxBook(String txBook) {
        this.txBook = txBook;
    }

    public String getTxReturnDate() {
        return txReturnDate;
    }

    public void setTxReturnDate(String txReturnDate) {
        this.txReturnDate = txReturnDate;
    }


    public String getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(String txAmount) {
        this.txAmount = txAmount;
    }


    public String getTxDeliveryDate() {
return txDeliveryDate;
}

public void setTxDeliveryDate(String txDeliveryDate) {
this.txDeliveryDate = txDeliveryDate;
}

public String getTxId() {
return txId;
}

public void setTxId(String txId) {
this.txId = txId;
}

public String getTxDate() {
return txDate;
}

public void setTxDate(String txDate) {
this.txDate = txDate;
}

public String getTxDeliverySts() {
return txDeliverySts;
}

public void setTxDeliverySts(String txDeliverySts) {
this.txDeliverySts = txDeliverySts;
}

public String getTxPaymentMode() {
return txPaymentMode;
}

public void setTxPaymentMode(String txPaymentMode) {
this.txPaymentMode = txPaymentMode;
}

public String getTxUser() {
return txUser;
}

public void setTxUser(String txUser) {
this.txUser = txUser;
}

public String getTxDelete() {
return txDelete;
}

public void setTxDelete(String txDelete) {
this.txDelete = txDelete;
}

public String getTxType() {
return txType;
}

public void setTxType(String txType) {
this.txType = txType;
}

}

