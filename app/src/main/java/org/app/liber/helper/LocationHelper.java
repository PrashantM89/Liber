package org.app.liber.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.List;
import java.util.Locale;

public class LocationHelper {
    private Context context;
    private static final int MY_PERMISSION_REQUEST_LOCATION = 1;

    public LocationHelper(Context c){
        this.context = c;
    }

    public String getLocation(double lat, double lon){
        String currentCity = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList;
        try{
            addressList = geocoder.getFromLocation(lat,lon, 1);
            if(addressList.size()>0){
                currentCity = addressList.get(0).getLocality();
            }
        }catch (Exception e){

        }
        return currentCity;
    }
}
