package com.example.simpleappwithmoderntechnologies.di

import com.example.simpleappwithmoderntechnologies.BuildConfig
import com.example.simpleappwithmoderntechnologies.model.Currency
import com.example.simpleappwithmoderntechnologies.model.Rate
import com.example.simpleappwithmoderntechnologies.repostiory.RatesApiService
import com.example.simpleappwithmoderntechnologies.repostiory.RatesRepository
import com.example.simpleappwithmoderntechnologies.repostiory.RatesRepositoryImpl
import com.example.simpleappwithmoderntechnologies.repostiory.dto.RatesDto
import com.example.simpleappwithmoderntechnologies.repostiory.mapper.Mapper
import com.example.simpleappwithmoderntechnologies.repostiory.mapper.RatesDtoToRateMapper
import com.example.simpleappwithmoderntechnologies.ui.rate.RatesViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    factory<RatesRepository> { RatesRepositoryImpl(get(), get()) }
    factory<Mapper<RatesDto, Rate>> { RatesDtoToRateMapper(Currency.USD, Currency.PLN) }
}

val viewModelModule = module {
    viewModel { RatesViewModel(get()) }
}

val networkModule = module {
    factory { provideOkHttpClient() }
    factory { provideRatesApiService(get()) }
    single { provideRetrofit(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl("https://api.exchangeratesapi.io").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(httpLoggingInterceptor)
    }
    return builder.build()
}

fun provideRatesApiService(retrofit: Retrofit): RatesApiService =
    retrofit.create(RatesApiService::class.java)
