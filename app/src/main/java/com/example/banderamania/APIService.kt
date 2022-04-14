package com.example.banderamania

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

public interface APIService {

    @GET("{countryCode}")
    fun getFullName(@Path("countryCode") countryCode: String): Call<Country>

    @GET("{countryName}")
    fun getCode(@Path("countryName") countryName: String): Call<Country>
}