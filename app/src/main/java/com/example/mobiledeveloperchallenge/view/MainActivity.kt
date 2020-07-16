package com.example.mobiledeveloperchallenge.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mobiledeveloperchallenge.R
import com.example.mobiledeveloperchallenge.model.CurrencyQuote
import com.example.mobiledeveloperchallenge.model.Quote
import com.example.mobiledeveloperchallenge.viewmodel.CurrencyViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Double.parseDouble

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: CurrencyViewModel
    lateinit var countryCodes: Array<String>
    var currencyQuotes = ArrayList<Quote>()
    var currentPosition: Int = 0
    lateinit var currencyRatesAdapter: CurrencyRatesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(CurrencyViewModel::class.java)

        currencyRecyclerView.apply {
            currencyRatesAdapter = CurrencyRatesAdapter(context)
            adapter = currencyRatesAdapter
            layoutManager = GridLayoutManager(context, 3)
        }

        viewModel.getDefaultCurrencies()

        observeCurrencies()
        observeCurrency()

        currencyEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                if (editable!!.length > 0) {
                    val number: String = editable.toString()
                    try {
                        val num = parseDouble(number)
                        currencyQuotes.forEach { quote ->
                            quote.updatedPrice =
                                quote.usdprice * num * currencyRatesAdapter.reference
                        }
                        currencyRatesAdapter.notifyDataSetChanged()
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })


        currenciesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {
                if (currencyQuotes.size != 0) {
                    currencyRatesAdapter.updateReference(
                        currencyQuotes.get(position).country,
                        parseDouble(currencyEditText.text.toString())
                    )
                }
            }
        }
    }

    private fun observeCurrencies() {
        viewModel.currencies.observe(this, Observer { currencyData ->
            countryCodes = currencyData.currencies.keys.toTypedArray()
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                countryCodes
            ) as SpinnerAdapter
            currenciesSpinner.adapter = adapter
            viewModel.refresh()
        })
        viewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                loadingScreen.visibility = View.VISIBLE
                screenData.visibility = View.GONE
            } else {
                loadingScreen.visibility = View.GONE
                screenData.visibility = View.VISIBLE
            }
        })
        viewModel.loadError.observe(this, Observer { isError ->
            if (isError) {
                errorScreenData.visibility = View.VISIBLE
                screenData.visibility = View.GONE
            } else {
                errorScreenData.visibility = View.GONE
                screenData.visibility = View.VISIBLE
            }
        })
    }

    private fun observeCurrency() {
        viewModel.currency.observe(this, Observer { currencyData ->
            currencyQuotes = ArrayList<Quote>()
            for (key in currencyData.quotes.keys) {
                var currency = key
                if (currency.length > 3) {
                    currency = key.replaceFirst(currencyData.source, "")
                }
                currencyQuotes.add(
                    Quote(
                        currency,
                        currencyData.quotes.get(key)!!
                    )
                )
            }
            currencyRatesAdapter.updateList(currencyQuotes)
            currenciesSpinner.setSelection(countryCodes.indexOf(currencyData.source))
        })
        viewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                loadingRates.visibility = View.VISIBLE
                currencyRecyclerView.visibility = View.GONE
            } else {
                loadingRates.visibility = View.GONE
                currencyRecyclerView.visibility = View.VISIBLE
            }
        })
        viewModel.loadError.observe(this, Observer { isError ->
            if (isError) {
                errorRates.visibility = View.VISIBLE
                currencyRecyclerView.visibility = View.GONE
            } else {
                errorRates.visibility = View.GONE
                currencyRecyclerView.visibility = View.VISIBLE
            }
        })
    }
}