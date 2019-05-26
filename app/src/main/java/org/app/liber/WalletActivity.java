package org.app.liber;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.app.liber.helper.DatabaseHelper;
import org.app.liber.helper.WalletTxn;

public class WalletActivity extends AppCompatActivity {

    TextView walletAmntText;
    WalletTxn walletTxn;
    DatabaseHelper databaseHelper;
    Button redeemBtn;
    LinearLayout walletLLayout;

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
        databaseHelper = new DatabaseHelper(getApplicationContext());
        walletAmntText = (TextView)findViewById(R.id.wallet_amnt_id);
        redeemBtn = (Button)findViewById(R.id.redeem_bttn_id);
        walletLLayout = (LinearLayout)findViewById(R.id.wallet_layout_id);
        walletTxn = new WalletTxn(databaseHelper);
        walletAmntText.setText(String.valueOf(walletTxn.getWalletAmnt("7338239977")));

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
