package org.app.liber.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.app.liber.LiberApiBase;
import org.app.liber.LiberEndpointInterface;
import org.app.liber.MainLiberActivity;
import org.app.liber.R;
import org.app.liber.pojo.UserPojo;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPVerification extends AppCompatActivity {

    private EditText OTP;
    private String mobile;
    private ProgressBar progressBar;
    private String verificationId;
    private FirebaseAuth mAuth;
    private Button verifyOTPBtn;
    private TextView label;
    private LiberEndpointInterface service;
    private SharedPreferences pref;
    private boolean exist = false;
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar)findViewById(R.id.otpVerifyProgressBarId);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);
        mobile = "+91"+getIntent().getStringExtra("mobile").trim();
        OTP = (EditText)findViewById(R.id.signup_input_otp_id);
        label = (TextView)findViewById(R.id.txt_label_id);
        verifyOTPBtn = (Button)findViewById(R.id.verify_otp_id);
        service = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        label.setText(getResources().getString(R.string.otp_verify_title2)+" "+mobile);

        i = getIntent();

        sendOTPVerificationCode(mobile);

        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(TextUtils.isEmpty(OTP.getText().toString())){
                Toast.makeText(getApplicationContext(),"Please enter OTP to proceed.",Toast.LENGTH_LONG).show();
            }else{
                verify(OTP.getText().toString().trim());

            }

            }
        });
    }


    private void verify(String code ){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInwithCredential(credential);
    }

    private void signInwithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(i.getBooleanExtra("exists",false)){
                        Intent intent = new Intent(getApplicationContext(), MainLiberActivity.class);
                        intent.putExtra("mobile",mobile);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                        intent.putExtra("mobile",mobile);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Error while verification: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void sendOTPVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code =  phoneAuthCredential.getSmsCode();

            if(code != null){
                OTP.setText(code);
                verify(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };


}
