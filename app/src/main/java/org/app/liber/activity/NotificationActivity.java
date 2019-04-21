package org.app.liber.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.app.liber.R;
import org.app.liber.adapter.NotificationAdapter;
import org.app.liber.adapter.RecyclerViewAdapter;
import org.app.liber.adapter.TransactionAdapter;
import org.app.liber.model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    NotificationAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.notification_toolbar_id);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backspace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.notification_recycler_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<NotificationModel> lstNotify = new ArrayList<>();
        lstNotify.add(new NotificationModel("Liked the app? Refer your fellow book readers and earn brownie points in your wallet. Spread the joy of reading.","12 Apr,19"));
        lstNotify.add(new NotificationModel("Liked the app? Refer your fellow book readers and earn brownie points in your wallet. Spread the joy of reading.","10 Jan,19"));
        lstNotify.add(new NotificationModel("Liked the app? Refer your fellow book readers and earn brownie points in your wallet. Spread the joy of reading.","08 May,19"));
        recyclerViewAdapter = new NotificationAdapter(getApplicationContext(),lstNotify);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
