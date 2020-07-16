package com.example.mobiledeveloperchallenge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mobiledeveloperchallenge.model.Currencies
import com.example.mobiledeveloperchallenge.model.CurrencyApiService
import com.example.mobiledeveloperchallenge.model.CurrencyQuote
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {

    val apiService = CurrencyApiService(application.applicationContext)
    val disposable = CompositeDisposable()
    val currency = MutableLiveData<CurrencyQuote>()
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
                .subscribeWith(object : DisposableSingleObserver<CurrencyQuote>() {
                    override fun onSuccess(t: CurrencyQuote) {
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