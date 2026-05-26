package com.example.pecheconnect;

import java.util.List;

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

    @GET("dashboard/{userId}")
    Call<DashboardResponse> getDashboardData(@Path("userId") int userId);

    // Corrigé : Enlevé le "/" au début pour correspondre au style de tes autres routes
    @POST("casiers/remonter")
    Call<RemonteResponse> remonterCasier(@Body RemonteRequest request);

    @POST("alertes/effacer")
    Call<RemonteResponse> effacerAlertes(@Body RemonteRequest request);

    @GET("mesures/alertes")
    Call<List<AlerteItem>> getAlertes();
}