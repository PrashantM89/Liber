package org.app.liber.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.app.liber.LiberApiBase;
import org.app.liber.LiberEndpointInterface;
import org.app.liber.R;
import org.app.liber.pojo.UserPojo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText mobileNo;
    private Button sendOTPBtn;
    private LiberEndpointInterface service;
    private SharedPreferences pref;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobileNo = (EditText)findViewById(R.id.signup_input_mobile_id);
        sendOTPBtn = (Button)findViewById(R.id.send_otp_bttn_id);
        service = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        sendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mobileNo.getText()) || mobileNo.getText().toString().length()<10){
                    Toast.makeText(getApplicationContext(),"Mobile number is required",Toast.LENGTH_SHORT).show();
                    mobileNo.requestFocus();
                    return;
                }
                existingUser();
            }
        });
    }


    private void existingUser() {
        Call<UserPojo> call = service.getUserDetails("+91"+mobileNo.getText().toString().trim());

        call.enqueue(new Callback<UserPojo>() {
            @Override
            public void onResponse(Call<UserPojo> call, Response<UserPojo> response) {
                Intent intent = new Intent(getApplicationContext(), OTPVerification.class);
                intent.putExtra("mobile",mobileNo.getText().toString());
                sharedPreferencesEditor.putString("USER_MOB", "+91"+mobileNo.getText().toString().trim());
                sharedPreferencesEditor.apply();
                if(response.code() == 200){

                    if(response.body().getUmob().isEmpty() || response.body().getUmob().equals("") || response.body().getUmob().equals(null)){
                        intent.putExtra("exists",false);
                    }else{
                        intent.putExtra("exists",true);
                        sharedPreferencesEditor.putString("USER_NAME", response.body().getUname().trim());
                        sharedPreferencesEditor.apply();
                        sharedPreferencesEditor.putString("USER_CITY", response.body().getUcity().trim());
                        sharedPreferencesEditor.apply();
                    }

                }else{

                    //Toast.makeText(getApplicationContext(),"Failed pulling user details.",Toast.LENGTH_LONG).show();
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<UserPojo> call, Throwable t) {

                //Toast.makeText(getApplicationContext(),"Failed pulling user details.",Toast.LENGTH_LONG).show();
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
