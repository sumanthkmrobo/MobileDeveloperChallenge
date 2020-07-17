package com.example.mobiledeveloperchallenge.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiledeveloperchallenge.R
import com.example.mobiledeveloperchallenge.model.data.Quote
import kotlinx.android.synthetic.main.currency_rate_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class CurrencyRatesAdapter(private var context: Context) :
    RecyclerView.Adapter<CurrencyRatesAdapter.ViewHolder>() {
    private var ratesArray: ArrayList<Quote> = ArrayList()
    private var reference: Double = 1.0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.currency_rate_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = ratesArray.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rate = ratesArray[position]
        val decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.CEILING
        holder.itemView.currencyRate.text = if (rate.updatedPrice == 0.0) {
            decimalFormat.format(rate.usd_price).toString()
        } else {
            decimalFormat.format(rate.updatedPrice).toString()
        }
        holder.itemView.currencyCountry.text = rate.country
    }

    fun updateList(quotes: ArrayList<Quote>) {
        ratesArray = quotes
        notifyDataSetChanged()
    }

    fun updateReference(selectedItem: String, num: Double) {
        ratesArray.forEach { quote ->
            if (quote.country == selectedItem) {
                reference = 1 / quote.usd_price
            }
        }
        updatePricing(num)
    }

    fun updatePricing(num: Double) {
        ratesArray.forEach { quote ->
            quote.updatedPrice = quote.usd_price * num * reference
        }
        notifyDataSetChanged()
    }
}