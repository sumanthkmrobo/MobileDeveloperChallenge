package com.example.mobiledeveloperchallenge.model.network

import com.example.mobiledeveloperchallenge.model.data.Currencies
import com.example.mobiledeveloperchallenge.model.data.CurrencyQuote
import io.reactivex.Single
import retrofit2.http.GET

interface CurrencyApi {

    @GET("live?access_key=1c91367ac118e441453e9fa8add2f8b4")
    fun getCurrencyRates(): Single<CurrencyQuote>

    @GET("list?access_key=1c91367ac118e441453e9fa8add2f8b4")
    fun getCurrencies(): Single<Currencies>
}