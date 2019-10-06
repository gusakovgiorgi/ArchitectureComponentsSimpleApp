package com.example.simpleappwithmoderntechnologies.repostiory.mapper

import com.example.simpleappwithmoderntechnologies.model.Currency
import com.example.simpleappwithmoderntechnologies.model.Rate
import com.example.simpleappwithmoderntechnologies.repostiory.dto.RatesDto
import com.example.simpleappwithmoderntechnologies.repostiory.exception.InvalidBaseCurrencyException
import com.example.simpleappwithmoderntechnologies.repostiory.exception.NoQuoteCurrencyException
import java.math.BigDecimal

class RatesDtoToRateMapper(val baseCurrency: Currency, val quoteCurrency: Currency) :
    Mapper<RatesDto, Rate> {

    @Throws(InvalidBaseCurrencyException::class, NoQuoteCurrencyException::class)
    override fun map(input: RatesDto): Rate {
        if (input.base != baseCurrency)
            throw InvalidBaseCurrencyException(
                "expected $baseCurrency base currency but got ${input.base}"
            )

        return input.rates.asSequence().filter {
            it.key == quoteCurrency.name
        }.map {
            Rate(baseCurrency, quoteCurrency, BigDecimal(it.value))
        }.firstOrNull()
            ?: throw NoQuoteCurrencyException("can't find quote currency $quoteCurrency")
    }
}