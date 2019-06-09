package org.app.liber;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.app.liber.helper.DatabaseHelper;
import org.app.liber.helper.ToastUtil;
import org.app.liber.helper.WalletTxn;
import org.app.liber.pojo.WalletPojo;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {

    private TextView walletAmntText;
    private DatabaseHelper databaseHelper;
    private Button redeemBtn;
    private LinearLayout walletLLayout;
    private ProgressDialog progressDialog;
    private LiberEndpointInterface service;
    private SharedPreferences pref;
    private String userName;
    private String userMob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backspace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userName = pref.getString("USER_NAME","Unknown");
        userMob = pref.getString("USER_MOB","Unknown");
        databaseHelper = new DatabaseHelper(getApplicationContext());
        walletAmntText = (TextView)findViewById(R.id.wallet_amnt_id);
        redeemBtn = (Button)findViewById(R.id.redeem_bttn_id);
        walletLLayout = (LinearLayout)findViewById(R.id.wallet_layout_id);
        service = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class);

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage(getApplicationContext().getString(R.string.wallet_processing_label));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        Call<ArrayList<WalletPojo>> call = service.getUserWalletData(userMob);
        call.enqueue(new Callback<ArrayList<WalletPojo>>() {

            @Override
            public void onResponse(Call<ArrayList<WalletPojo>> call, Response<ArrayList<WalletPojo>> response) {
                progressDialog.dismiss();
                Double amount = 0.0;
                String totalWalletAmount = "";

                for(WalletPojo wallet:response.body()){
                    amount += Double.parseDouble(wallet.getWamntAdded());
                }
                totalWalletAmount = String.valueOf(amount);

                walletAmntText.setText(totalWalletAmount);
            }

            @Override
            public void onFailure(Call<ArrayList<WalletPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(walletLLayout, "We have received your request. Money will be transferred to your Paytm account within a week time.", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                redeemBtn.setEnabled(false);
                            }
                        });

                snackbar.show();
            }
        });
    }
}
