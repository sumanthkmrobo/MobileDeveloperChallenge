package com.example.mobiledeveloperchallenge.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobiledeveloperchallenge.model.Currencies
import com.example.mobiledeveloperchallenge.model.CurrencyQuote
import com.example.mobiledeveloperchallenge.model.CurrencyApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class CurrencyViewModel : ViewModel() {

    val apiService = CurrencyApiService()
    val disposable = CompositeDisposable()
    val currency = MutableLiveData<CurrencyQuote>()
    val currencies = MutableLiveData<Currencies>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchFromRemote()
    }

    fun getDefaultCurrencies(){
        getCurrencies()
    }

    fun getCurrencies(){
        loading.value = true
        disposable.add(
            apiService.getCurrencies()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<Currencies>(){
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

    private fun fetchFromRemote() {
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