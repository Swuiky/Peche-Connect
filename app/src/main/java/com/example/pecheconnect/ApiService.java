package com.example.pecheconnect;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("register")
    Call<AuthResponse> register(@Body AuthRequest body);

    @POST("login")
    Call<AuthResponse> login(@Body AuthRequest body);

    // Nouvelle route :
    @GET("dashboard/{userId}")
    Call<DashboardResponse> getDashboardData(@Path("userId") int userId);
}