package com.example.mobiledeveloperchallenge.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyApiService {
    private val BASE_URL = "http://api.currencylayer.com/"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(CurrencyApi::class.java)

    fun getCurrencyRates(): Single<CurrencyQuote> {
        return api.getCurrencyRates()
    }

    fun getCurrencies(): Single<Currencies> {
        return api.getCurrencies()
    }

}