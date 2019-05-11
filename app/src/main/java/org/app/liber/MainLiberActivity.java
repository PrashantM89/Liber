package org.app.liber;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
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
import org.app.liber.Service.LocationService;
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
    private LinearLayout linearLayout;
    private String city;
    private SharedPreferences pref;
    private boolean doubleBackToExitPressedOnce = false;
    private LocationHelper locationHelper;
    private BroadcastReceiver broadcastReceiver;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);
        linearLayout = (LinearLayout)findViewById(R.id.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        locationHelper = new LocationHelper(getApplicationContext());

        locationTxt = (TextView) findViewById(R.id.toolbar_location_id);
     
       if(!runTimePermission()){
           enableLocation();
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

        adapter.addFragment(new LibraryFragment(), "Library");
        adapter.addFragment(new BookshelveFragment(), "Bookshelf");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private boolean runTimePermission() {

        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION }, 100);
            onResume();
            return true;
        }
        return false;
    }

    @Override
    public void onResume(){
        super.onResume();

        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String loc = intent.getStringExtra("cordinates");
                    String longitude =loc.substring(0,loc.indexOf(" "));
                    String latitude = loc.substring(loc.indexOf(" "),loc.length());
                    if(longitude != null && latitude != null){
                        city = locationHelper.getLocation(Double.valueOf(latitude), Double.valueOf(longitude));
                        locationTxt.setText(city);
                        sharedPreferencesEditor.putString("user_location",city.trim().toString());
                        sharedPreferencesEditor.apply();
                        disableLocation();
                    }
                }
            };
        }

        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enableLocation();
            }else {
                runTimePermission();
            }
        }
    }

    private void enableLocation() {
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        startService(i);
    }


    private void disableLocation() {
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        stopService(i);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
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
