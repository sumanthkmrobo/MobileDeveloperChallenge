package com.example.mobiledeveloperchallenge.model.data

data class CurrencyRate(
    var source: String,
    var quotes: Map<String, Double>
)

data class Quote(
    var country: String,
    var usd_price: Double,
    var updatedPrice: Double = 0.0
)