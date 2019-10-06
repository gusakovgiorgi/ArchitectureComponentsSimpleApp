package com.example.simpleappwithmoderntechnologies.repostiory

import com.example.simpleappwithmoderntechnologies.model.Currency
import com.example.simpleappwithmoderntechnologies.model.Rate
import com.example.simpleappwithmoderntechnologies.repostiory.dto.RatesDto
import com.example.simpleappwithmoderntechnologies.repostiory.exception.InvalidBaseCurrencyException
import com.example.simpleappwithmoderntechnologies.repostiory.exception.NoQuoteCurrencyException
import com.example.simpleappwithmoderntechnologies.repostiory.mapper.Mapper
import java.net.SocketTimeoutException

interface RatesRepository {
    suspend fun getUsdPlnRate(): Result<Rate>
}

class RatesRepositoryImpl(
    private val ratesApiService: RatesApiService,
    private val ratesDataMapper: Mapper<RatesDto, Rate>
) : RatesRepository {
    override suspend fun getUsdPlnRate(): Result<Rate> {
        return try {
            val result = ratesApiService.getLatestRates(Currency.USD.name)
            if (result.isSuccessful) {
                Result.Success(ratesDataMapper.map(result.body()!!))
            } else {
                // todo inspect failure
                Result.Failure(FailReason.CONNECTION_ERROR)
            }
        } catch (e: SocketTimeoutException) {
            Result.Failure(FailReason.CONNECTION_ERROR)
        } catch (e: InvalidBaseCurrencyException) {
            Result.Failure(FailReason.NOT_FOUND_BASE_CURRENCY)
        } catch (e: NoQuoteCurrencyException) {
            Result.Failure(FailReason.NOT_FOUND_QUOTE_CURRENCY)
        } catch (thw: Throwable) {
            Result.Failure(FailReason.UNKNOWN)
        }
    }
}