package org.app.liber;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.app.liber.adapter.TransactionAdapter;
import org.app.liber.helper.DatabaseHelper;
import org.app.liber.pojo.TransactionPojo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private LiberEndpointInterface service;
    List<TransactionPojo> lstUserTxn;
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
        service = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class);
        recyclerView = (RecyclerView)findViewById(R.id.tx_recycler_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage(getApplicationContext().getString(R.string.processing_txn_label));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        addUserTransactionFromDB();
    }

    public void addUserTransactionFromDB(){

        lstUserTxn = new ArrayList<>();

        Call<ArrayList<TransactionPojo>> call = service.getAllUserTxns("7338239977");
        call.enqueue(new Callback<ArrayList<TransactionPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<TransactionPojo>> call, Response<ArrayList<TransactionPojo>> response) {

                if(response.code() == 200){
                    progressDialog.dismiss();
                    //errorLayout.setVisibility(View.GONE);
                    recyclerViewAdapter = new TransactionAdapter(getApplicationContext(),lstUserTxn = response.body());
                    recyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.notifyDataSetChanged();
                }else{
                    progressDialog.dismiss();
                    //errorLayout.setVisibility(View.VISIBLE);

//                    tap2Refresh.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            loadLibraryData();
//                        }
//                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TransactionPojo>> call, Throwable t) {
                progressDialog.dismiss();
//                errorLayout.setVisibility(View.VISIBLE);
//                tap2Refresh.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        loadLibraryData();
//                    }
//                });
            }
        });
    }
}
