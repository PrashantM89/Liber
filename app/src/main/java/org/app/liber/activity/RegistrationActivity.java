package org.app.liber.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import org.app.liber.LiberApiBase;
import org.app.liber.LiberEndpointInterface;
import org.app.liber.MainLiberActivity;
import org.app.liber.R;
import org.app.liber.helper.DatabaseHelper;
import org.app.liber.helper.DateUtil;
import org.app.liber.pojo.UserPojo;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity  {

    private EditText userEmail;
    private EditText userName;
    private EditText userAdd2;
    private EditText userAdd1;
    private int AUTOCOMPLETE_REQUEST_CODE = 1;
    private boolean validationStatus = false;
    private Button saveUsrDetailsBtn;
    private ProgressBar progressBar;
    private DatabaseHelper databaseHelper;
    private Geocoder geocoder;
    private String city;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private UserPojo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_auth_layout);

        userAdd1 = (EditText) findViewById(R.id.signup_input_address);
        userName = (EditText) findViewById(R.id.signup_input_name);
        userEmail = (EditText) findViewById(R.id.signup_input_email);
        userAdd2 = (EditText) findViewById(R.id.signup_input_add2);
        saveUsrDetailsBtn = (Button)findViewById(R.id.btn_save_usr_id);
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        progressBar = (ProgressBar) findViewById(R.id.userRegProgressBarId);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        String apiKey = getString(R.string.api_key);

        if (apiKey.equals("")) {
            Toast.makeText(this, "Error while initializing api key.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        final List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        final Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        userAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        saveUsrDetailsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                validationStatus = validateUserEntries();
                progressBar.setVisibility(View.VISIBLE);
                if(validationStatus){
                    user = new UserPojo();
                    user.setUdelete("N");
                    user.setUlastUpdate(DateUtil.getDate(Calendar.getInstance().getTime()));
                    user.setUsignupDate(DateUtil.getDate(Calendar.getInstance().getTime()));
                    user.setUaddress(userAdd2.getText().toString() +", "+userAdd1.getText().toString());
                    user.setUmob(getIntent().getStringExtra("mobile").trim());
                    user.setUpin("");
                    user.setUcity(city);
                    user.setUname(userName.getText().toString());
                    user.setUemail(userEmail.getText().toString());

                    Call<ResponseBody> call = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class).createNewUser(user);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
         //                   databaseHelper.addUser(user);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Delivery details Saved",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainLiberActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Failed saving delivery details : "+t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateUserEntries() {

        if(TextUtils.isEmpty(userName.getText()) || TextUtils.isEmpty(userEmail.getText()) || TextUtils.isEmpty(userAdd1.getText()) || TextUtils.isEmpty(userAdd2.getText())){
            Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                userAdd1.setText(place.getName()+" "+place.getAddress());
                LatLng quariedLocation = place.getLatLng();
                Double lat = quariedLocation.latitude;
                Double lon = quariedLocation.longitude;

                List <Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(lat, lon, 1);
                    city = addresses.get(0).getLocality();
                    sharedPreferencesEditor.putString("USER_CITY", city);
                    sharedPreferencesEditor.apply();
                    sharedPreferencesEditor.putString("USER_NAME", userName.getText().toString().trim());
                    sharedPreferencesEditor.apply();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"Error: Can't fetch city. ",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(getApplicationContext(),"An error occurred: " + validationStatus,Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

}

