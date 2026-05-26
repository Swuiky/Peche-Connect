package com.example.pecheconnect;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:3000/";
    private static ApiService apiService;

    public static ApiService getInstance() {
        if (apiService == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    // --- TES MÉTHODES D'APPEL ---

    public static void checkUser(String email, String password, Callback<AuthResponse> callback) {
        getInstance().login(new AuthRequest(email, password)).enqueue(callback);
    }

    //Méthode statique pour correspondre au reste de la classe
    public static Call<RemonteResponse> effacerAlertes(RemonteRequest request) {
        return getInstance().effacerAlertes(request);
    }
}