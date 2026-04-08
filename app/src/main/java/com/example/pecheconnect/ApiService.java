package com.example.pecheconnect;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("register")
    Call<AuthResponse> register(@Body AuthRequest body);

    @POST("login")
    Call<AuthResponse> login(@Body AuthRequest body);
}