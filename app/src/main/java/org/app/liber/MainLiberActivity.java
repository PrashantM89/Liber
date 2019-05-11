package org.app.liber;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.app.liber.activity.NotificationActivity;
import org.app.liber.adapter.ViewPagerAdapter;
import org.app.liber.helper.DatabaseHelper;
import org.app.liber.helper.LocationHelper;

import de.cketti.mailto.EmailIntentBuilder;

public class MainLiberActivity extends AppCompatActivity implements BookListFragment.OnListFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Toolbar toolbar;
    private AlertDialog.Builder locationAlert;
    private TextView locationTxt;
    //UserSessionManager session;
    LinearLayout linearLayout;
    DatabaseHelper databaseHelper;
    Intent i;
    String city;
    private SharedPreferences pref;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);
        linearLayout = (LinearLayout)findViewById(R.id.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        locationTxt = (TextView) findViewById(R.id.toolbar_location_id);
        //check for user permission to access his location
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainLiberActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(MainLiberActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            }else {
                ActivityCompat.requestPermissions(MainLiberActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            }
        }else{
            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LocationHelper locationHelper = new LocationHelper(getApplicationContext());
            try{
                city = locationHelper.getLocation(location.getLatitude(),location.getLongitude());
                locationTxt.setText(city);
                sharedPreferencesEditor.putString("USER_LOCATION",city);
                sharedPreferencesEditor.apply();
                Toast.makeText(getApplicationContext(),"Location Found: "+city,Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Location Not Found!",Toast.LENGTH_LONG).show();
            }
        }

//        i = getIntent();
//        city = i.getStringExtra("location");

        String location =pref.getString("USER_LOCATION","LOCATION_NOT_FOUND");

        if(location.isEmpty() || location.equals("LOCATION_NOT_FOUND") || locationTxt.getText().equals("")){
            Toast.makeText(getApplicationContext(),"Please select Location.",Toast.LENGTH_LONG).show();
            locationTxt.setText("Select Location");
        }


        if (!pref.getBoolean(
                "COMPLETED_ONBOARDING_PREF_NAME", false)) {
            Intent i = new Intent(this, OnboardingActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
            this.finish();
        }

        sharedPreferencesEditor.putBoolean(
                "COMPLETED_ONBOARDING_PREF_NAME", true);
        sharedPreferencesEditor.apply();

        databaseHelper = new DatabaseHelper(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        locationAlert = new AlertDialog.Builder(MainLiberActivity.this);
        locationAlert.setTitle("Oops!, We are not yet in your city")
                .setMessage("We'll come back to you once we are in your city.")
                .setCancelable(false)
                .setPositiveButton("Close Liber", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

        AlertDialog alert = locationAlert.create();

//        if(city.equalsIgnoreCase("Hyderabad")){
//            alert.show();
//        }

//        session = new UserSessionManager(getApplicationContext());
//        session.checkLogin();

        //Add fragment here
        adapter.addFragment(new LibraryFragment(), "Library");
        adapter.addFragment(new BookshelveFragment(), "Bookshelf");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share_id:
                Intent intentShare = new Intent("android.intent.action.SEND");
                intentShare.setType("text/plain");
                intentShare.putExtra("android.intent.extra.TEXT", "Share Liber app among your fellow readers and earn by sharing.");
                startActivity(Intent.createChooser(intentShare, "Share"));
                break;
            case R.id.notification_id:
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                break;
            case R.id.logout_id:
//                session.logoutUser();
//                finish();
                break;
            case R.id.aboutus_id:
                Toast.makeText(getApplicationContext(), "Functionality coming soon.", Toast.LENGTH_LONG).show();
                break;
            case R.id.contactus_id:
                Intent emailIntent = EmailIntentBuilder.from(getApplicationContext())
                        .to("sandwista@gmail.com")
                        .subject("Feedback")
                        .build();
                startActivity(emailIntent);
                break;
            case R.id.order_sum_id:
                startActivity(new Intent(getApplicationContext(), TransactionList.class));
                break;
            case R.id.wallet_id:
                startActivity(new Intent(getApplicationContext(), WalletActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addBookAgainToShelf(View v) {
        Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
       // Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar
                .make(linearLayout, "Please click BACK again to exit", Snackbar.LENGTH_SHORT);
        snackbar.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
