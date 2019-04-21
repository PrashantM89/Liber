package org.app.liber.model;

public class NotificationModel {
    String notifyText;
    String notifyTime;

    public NotificationModel(String notifyText, String notifyTime) {
        this.notifyText = notifyText;
        this.notifyTime = notifyTime;
    }

    public String getNotifyText() {
        return notifyText;
    }

    public void setNotifyText(String notifyText) {
        this.notifyText = notifyText;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }
}
