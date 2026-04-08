package com.example.pecheconnect;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // Vérifie bien cette IP !
    private static final String BASE_URL = "http://10.0.2.2:3000/";
    private static ApiService apiService;

    public static ApiService getInstance() {
        if (apiService == null) {
            // On ajoute un LOG pour voir ce qui passe dans la console d'Android Studio (Logcat)
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client) // On ajoute le client avec logs
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    public static void insertUser(String email, String password, Callback<AuthResponse> callback) {
        getInstance().register(new AuthRequest(email, password)).enqueue(callback);
    }

    public static void checkUser(String email, String password, Callback<AuthResponse> callback) {
        getInstance().login(new AuthRequest(email, password)).enqueue(callback);
    }
}