package org.app.liber.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.app.liber.R;

public class LoginActivity extends AppCompatActivity {

    private EditText mobileNo;
    private Button sendOTPBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobileNo = (EditText)findViewById(R.id.signup_input_mobile_id);
        sendOTPBtn = (Button)findViewById(R.id.send_otp_bttn_id);


        sendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mobileNo.getText()) || mobileNo.getText().toString().length()<10){
                    Toast.makeText(getApplicationContext(),"Mobile number is required",Toast.LENGTH_SHORT).show();
                    mobileNo.requestFocus();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), OTPVerification.class);
                intent.putExtra("mobile",mobileNo.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();
    }
}
