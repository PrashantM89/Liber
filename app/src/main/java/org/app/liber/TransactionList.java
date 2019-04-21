package org.app.liber;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.app.liber.adapter.TransactionAdapter;
import org.app.liber.helper.DatabaseHelper;
import org.app.liber.model.UserTransactionModel;

import java.util.ArrayList;
import java.util.List;

public class TransactionList extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<UserTransactionModel> lstUserTxn;
    DatabaseHelper databaseHelper;
    TransactionAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backspace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.tx_recycler_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addUserTransactionFromDB();
    }

    public void addUserTransactionFromDB(){
        databaseHelper = new DatabaseHelper(getApplicationContext());
        Cursor result = databaseHelper.getTxData();
        lstUserTxn = new ArrayList<>();
        while (result.moveToNext()){
            UserTransactionModel b = new UserTransactionModel();
            b.setTxId(result.getString(result.getColumnIndex(result.getColumnName(0).toString())));
            b.setTxDate(result.getString(result.getColumnIndex(result.getColumnName(1).toString())));
            b.setTxStatus(result.getString(result.getColumnIndex(result.getColumnName(2).toString())));
            b.setTxMode(result.getString(result.getColumnIndex(result.getColumnName(3).toString())));
            b.setDeliveryStatus(result.getString(result.getColumnIndex(result.getColumnName(4).toString())));

            lstUserTxn.add(b);
        }

        recyclerViewAdapter = new TransactionAdapter(getApplicationContext(),lstUserTxn);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
