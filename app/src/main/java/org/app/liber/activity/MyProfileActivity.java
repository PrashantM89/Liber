package org.app.liber.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.libraries.places.api.model.Place;

import org.app.liber.R;
import org.app.liber.fragment.ProfileInputDialogFragment;
import org.app.liber.helper.ToastUtil;

public class MyProfileActivity extends AppCompatActivity{

    private LinearLayout mFirstName;
    private LinearLayout mLastName;
    private LinearLayout mDisplayName;
    private LinearLayout mEmail;
    private LinearLayout mAddress;

    private EditText firstName;
    private EditText lastName;
    private EditText displayName;
    private EditText email;
    private TextView address;

    private Button saveProfileBtn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        mFirstName = (LinearLayout) findViewById(R.id.first_name_row);
        mLastName = (LinearLayout) findViewById(R.id.last_name_row);
        mDisplayName = (LinearLayout) findViewById(R.id.display_name_row);
        mEmail= (LinearLayout) findViewById(R.id.emailid_row);
        mAddress = (LinearLayout) findViewById(R.id.address_row);

        mAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        saveProfileBtn = (Button)findViewById(R.id.save_profile_id);

        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        displayName = (EditText) findViewById(R.id.display_name);
        email = (EditText) findViewById(R.id.emailid_name);
        address = (TextView) findViewById(R.id.address_name);


        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(firstName.getText().equals("") || firstName == null){
                        ToastUtil.showToast(getApplicationContext(),"First name can be empty.");
                        firstName.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                }
            }
        });

        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtil.showToast(getApplicationContext(),firstName.getText().toString()+" "+lastName.getText().toString()+" "+displayName.getText().toString()+" "+email.getText().toString()+" "+address.getText().toString()+" ");

            }
        });
    }
}
