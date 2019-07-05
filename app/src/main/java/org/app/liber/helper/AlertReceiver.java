package org.app.liber.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import org.app.liber.R;

import java.util.Date;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(context )
                .setSmallIcon(R.mipmap.liber_launcher)
                .setContentTitle("Liber: Due date reminder")
                .setContentText("Return date for the book is tomorrow. Our delivery person will contact you for the same.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());

    }
}
