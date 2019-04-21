package org.app.liber.model;

import java.io.Serializable;
import java.util.List;

public class UserTransactionModel implements Serializable {
    private String txId;
    private List<Book> books;
    private String txDate;
    private String txStatus;
    private String txMode;
    private String deliveryStatus;


    public UserTransactionModel(String txId, List<Book> books, String txDate, String txStatus, String txMode, String deliveryStatus) {
        this.txId = txId;
        this.books = books;
        this.txDate = txDate;
        this.txStatus = txStatus;
        this.txMode = txMode;
        this.deliveryStatus = deliveryStatus;
    }

    public UserTransactionModel(){}

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getTxDate() {
        return txDate;
    }

    public void setTxDate(String txDate) {
        this.txDate = txDate;
    }

    public String getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(String txStatus) {
        this.txStatus = txStatus;
    }

    public String getTxMode() {
        return txMode;
    }

    public void setTxMode(String txMode) {
        this.txMode = txMode;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
