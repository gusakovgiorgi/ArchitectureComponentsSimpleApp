package com.example.simpleappwithmoderntechnologies.repostiory

import com.example.simpleappwithmoderntechnologies.di.appModule
import com.example.simpleappwithmoderntechnologies.properJson
import com.example.simpleappwithmoderntechnologies.repostiory.exception.InvalidBaseCurrencyException
import com.example.simpleappwithmoderntechnologies.unProperJson
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get

class RatesRepositoryImplTest : KoinTest {

    private lateinit var repository: RatesRepositoryImpl

    @After
    fun after() {
        stopKoin()
    }


    @Test
    fun receiveProperJson_resultIsSuccessAndValueNotNull() {
        startKoinWithFakeApiService(CorrectRatesAPiService(properJson))
        runBlocking {
            val result = repository.getUsdPlnRate()
            assertTrue(result is Result.Success)
            assertThat((result as Result.Success).value, notNullValue())
        }
    }

    @Test(expected = InvalidBaseCurrencyException::class)
    fun receivedBadJson_throwException() {
        startKoinWithFakeApiService(CorrectRatesAPiService(unProperJson))
        runBlocking {
            val result = repository.getUsdPlnRate()
            assertTrue(result is Result.Success)
            assertThat((result as Result.Success).value, notNullValue())
        }
    }

    private fun startKoinWithFakeApiService(fakeRatesAPiService: RatesApiService) {
        startKoin {
            modules(listOf(appModule, module {
                factory { fakeRatesAPiService }
            }))
        }
        repository = RatesRepositoryImpl(get(), get())
    }
}