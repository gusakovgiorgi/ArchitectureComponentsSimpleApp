package com.example.simpleappwithmoderntechnologies.repostiory

import com.example.simpleappwithmoderntechnologies.repostiory.dto.RatesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApiService {
    /**
     *@param baseCurrency alphabetic currency code in ISO 4217
     */
    @GET("latest")
    suspend fun getLatestRates(@Query("base") baseCurrency: String): Response<RatesDto>
}