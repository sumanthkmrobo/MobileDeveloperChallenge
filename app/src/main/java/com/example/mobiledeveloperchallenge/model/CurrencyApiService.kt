package com.example.mobiledeveloperchallenge.model

import android.content.Context
import io.reactivex.Single
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyApiService(var context: Context) {
    private val BASE_URL = "http://api.currencylayer.com/"

    val myCache = Cache(context.cacheDir, (10 * 1024 * 1024).toLong())


    private fun loadApi(duration: Long): CurrencyApi {
        val okHttpClient = OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor { chain ->
                val request = chain.request()
                request.newBuilder()
                    .header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + 60 * duration
                    )
                    .build()
                chain.proceed(request)
            }
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(CurrencyApi::class.java)

    }

    fun getCurrencyRates(): Single<CurrencyQuote> {
        return loadApi(30).getCurrencyRates()
    }

    fun getCurrencies(): Single<Currencies> {
        return loadApi(60 * 24 * 7 * 4).getCurrencies()
    }


}