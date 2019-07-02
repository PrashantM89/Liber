package org.app.liber.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.libraries.places.api.model.Place;

import org.app.liber.LiberApiBase;
import org.app.liber.LiberEndpointInterface;
import org.app.liber.R;
import org.app.liber.adapter.TransactionAdapter;
import org.app.liber.fragment.ProfileInputDialogFragment;
import org.app.liber.helper.ToastUtil;
import org.app.liber.pojo.TransactionPojo;
import org.app.liber.pojo.UserPojo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity{

    private EditText userEmail;
    private EditText userName;
    private EditText userAdd1;
    private EditText mob;
    private LiberEndpointInterface service;
    private Button updateProfileBtn;
    private ProgressDialog progressDialog;
    private SharedPreferences pref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        userAdd1 = (EditText) findViewById(R.id.signup_input_address);
        userName = (EditText) findViewById(R.id.signup_input_name);
        userEmail = (EditText) findViewById(R.id.signup_input_email);
        mob = (EditText)findViewById(R.id.signup_input_mob);
        updateProfileBtn = (Button)findViewById(R.id.update_profile_id);
        service = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage(getApplicationContext().getString(R.string.processing_txn_label));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        fetchUserDetails();

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private void fetchUserDetails() {

        Call<UserPojo> call = service.getUserDetails(pref.getString("USER_MOB","Unknown"));
        call.enqueue(new Callback<UserPojo>() {
            @Override
            public void onResponse(Call<UserPojo> call, Response<UserPojo> response) {

                if(response.code() == 200){
                    userEmail.setText(response.body().getUemail());
                    userAdd1.setText(response.body().getUaddress());
                    userName.setText(response.body().getUname());
                    mob.setText(response.body().getUmob());
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Failed pulling user details.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserPojo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Failed pulling user details.",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchUserDetails();
    }
}
