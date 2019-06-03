package org.app.liber;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiberApiBase {
    private static final String BASE_URL = "http://192.168.42.167:8080/LiberWebApi-1.0/rest/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {

        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
