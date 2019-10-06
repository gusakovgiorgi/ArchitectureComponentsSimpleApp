package com.example.simpleappwithmoderntechnologies.repostiory.mapper

import com.example.simpleappwithmoderntechnologies.model.Currency
import com.example.simpleappwithmoderntechnologies.model.Rate
import com.example.simpleappwithmoderntechnologies.repostiory.dto.RatesDto
import com.example.simpleappwithmoderntechnologies.repostiory.exception.InvalidBaseCurrencyException
import com.example.simpleappwithmoderntechnologies.repostiory.exception.NoQuoteCurrencyException
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class RatesDtoToRateMapperTest {

    private lateinit var ratesDtoToRateMapper: RatesDtoToRateMapper
    @Before
    fun createDtoMapper() {
        ratesDtoToRateMapper = RatesDtoToRateMapper(Currency.USD, Currency.PLN)
    }

    @Test
    fun inputCorrectDto_returnCorrectResult() {
        val correctRatesDto = RatesDto(
            mapOf("EUR" to "1.234", "UAH" to "1.4545", "PLN" to "5"),
            Currency.USD,
            "2019-10-04"
        )
        val expectedRate = Rate(Currency.USD, Currency.PLN, BigDecimal("5"))
        val result = ratesDtoToRateMapper.map(correctRatesDto)
        assertThat(expectedRate, `is`(result))
    }

    @Test(expected = InvalidBaseCurrencyException::class)
    fun inputDtoWithDifferentBaseCurrency_returnException() {
        val dtoWithDifferentBaseCurrency = RatesDto(
            mapOf("EUR" to "1.234", "UAH" to "1.4545", "PLN" to "5"),
            Currency.PLN,
            "2019-10-04"
        )
        ratesDtoToRateMapper.map(dtoWithDifferentBaseCurrency)
    }

    @Test(expected = NoQuoteCurrencyException::class)
    fun inputDtoWithoutQuoteCurrency_returnException() {
        val dtoWithoutQuoteCurrency = RatesDto(
            mapOf("EUR" to "1.234", "UAH" to "1.4545", "GEL" to "5"),
            Currency.USD,
            "2019-10-04"
        )
        ratesDtoToRateMapper.map(dtoWithoutQuoteCurrency)
    }
}