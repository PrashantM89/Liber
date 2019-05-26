package org.app.liber.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                //Place place = PlacePicker.getPlace(this, data);
                Place place = PlaceAutocomplete.getPlace(this, data);
                address.setText(String.format("%s",place.getAddress()));
            }
        }
    }

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
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
//                    Intent intent = (Intent) builder.build(MyProfileActivity.this);
//                    startActivityForResult(intent,1);

                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(MyProfileActivity.this);
                    startActivityForResult(intent,1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        saveProfileBtn = (Button)findViewById(R.id.save_profile_id);

        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        displayName = (EditText) findViewById(R.id.display_name);
        email = (EditText) findViewById(R.id.emailid_name);
        address = (TextView) findViewById(R.id.address_name);


        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtil.showToast(getApplicationContext(),firstName.getText().toString()+" "+lastName.getText().toString()+" "+displayName.getText().toString()+" "+email.getText().toString()+" "+address.getText().toString()+" ");

            }
        });
    }
}
