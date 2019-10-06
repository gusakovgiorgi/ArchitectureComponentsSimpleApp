package com.example.simpleappwithmoderntechnologies.repostiory

import com.example.simpleappwithmoderntechnologies.repostiory.dto.RatesDto
import com.google.gson.Gson
import retrofit2.Response

class CorrectRatesAPiService(private val jsonStr: String) : RatesApiService {
    override suspend fun getLatestRates(baseCurrency: String): Response<RatesDto> {
        return Response.success(Gson().fromJson(jsonStr, RatesDto::class.java))
    }
}