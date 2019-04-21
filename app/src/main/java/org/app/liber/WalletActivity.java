package org.app.liber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.app.liber.helper.DatabaseHelper;
import org.app.liber.helper.WalletTxn;

public class WalletActivity extends AppCompatActivity {

    TextView walletAmntText;
    WalletTxn walletTxn;
    DatabaseHelper databaseHelper;

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
        walletTxn = new WalletTxn(databaseHelper);
        walletAmntText.setText(String.valueOf(walletTxn.getWalletAmnt("7338239977")));
    }
}
