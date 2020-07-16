package com.example.mobiledeveloperchallenge.model.data

data class CurrencyQuote(
    var success: Boolean,
    var timestamp: Long,
    var source: String,
    var quotes: Map<String, Double>
)

data class Quote(
    var country: String,
    var usdprice: Double,
    var updatedPrice: Double = 0.0
)