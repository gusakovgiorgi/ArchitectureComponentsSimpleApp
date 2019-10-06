package com.example.simpleappwithmoderntechnologies.repostiory.dto

import com.example.simpleappwithmoderntechnologies.model.Currency
import com.google.gson.annotations.SerializedName

class RatesDto(
    val rates: Map<String, String>,
    val base: Currency, @SerializedName("date") val dateStr: String
)