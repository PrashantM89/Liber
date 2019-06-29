package org.app.liber.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.app.liber.MainLiberActivity;
import org.app.liber.R;

import java.util.concurrent.TimeUnit;

public class OTPVerification extends AppCompatActivity {

    private EditText OTP;
    private String mobile;
    private ProgressBar progressBar;
    private String verificationId;
    private FirebaseAuth mAuth;
    private Button verifyOTPBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar)findViewById(R.id.otpVerifyProgressBarId);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);
        mobile = getIntent().getStringExtra("mobile");
        OTP = (EditText)findViewById(R.id.signup_input_otp_id);
        verifyOTPBtn = (Button)findViewById(R.id.verify_otp_id);

        sendOTPVerificationCode(mobile);

        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verify(OTP.getText().toString().trim());
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
                    Intent intent = new Intent(getApplicationContext(), MainLiberActivity.class);
                    intent.putExtra("mobile",mobile);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Error while verification: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendOTPVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,3, TimeUnit.MINUTES, TaskExecutors.MAIN_THREAD, mCallBack);
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