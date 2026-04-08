package com.example.pecheconnect;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://172.29.21.106:3000/";
    private static ApiService apiService;

    public static ApiService getInstance() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
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