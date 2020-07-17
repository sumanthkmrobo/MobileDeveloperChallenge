package com.example.mobiledeveloperchallenge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mobiledeveloperchallenge.model.data.Currencies
import com.example.mobiledeveloperchallenge.model.network.CurrencyApiService
import com.example.mobiledeveloperchallenge.model.data.CurrencyRate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService =
        CurrencyApiService(
            application.applicationContext
        )
    private val disposable = CompositeDisposable()
    val currency = MutableLiveData<CurrencyRate>()
    val currencies = MutableLiveData<Currencies>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun getDefaultCurrencies() {
        loading.value = true
        disposable.add(
            apiService.getCurrencies()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Currencies>() {
                    override fun onSuccess(t: Currencies) {
                        currencies.value = t
                        loadError.value = false
                        loading.value = false
                    }
                    override fun onError(e: Throwable) {
                        loadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    fun refresh() {
        loading.value = true
        disposable.add(
            apiService.getCurrencyRates()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CurrencyRate>() {
                    override fun onSuccess(t: CurrencyRate) {
                        currency.value = t
                        loadError.value = false
                        loading.value = false
                    }
                    override fun onError(e: Throwable) {
                        loadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}