package org.app.liber.model;

public class WalletModel {

    private String id;
    private int amountAdd;
    private String userMob;
    private String txnDate;

    public WalletModel(int amountAdd, String userMob, String txnDate) {
        this.amountAdd = amountAdd;
        this.userMob = userMob;
        this.txnDate = txnDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmountAdd() {
        return amountAdd;
    }

    public void setAmountAdd(int amountAdd) {
        this.amountAdd = amountAdd;
    }

    public String getUserMob() {
        return userMob;
    }

    public void setUserMob(String userMob) {
        this.userMob = userMob;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }
}
