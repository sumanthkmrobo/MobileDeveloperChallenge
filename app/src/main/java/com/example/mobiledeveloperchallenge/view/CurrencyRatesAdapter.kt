package com.example.mobiledeveloperchallenge.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiledeveloperchallenge.R
import com.example.mobiledeveloperchallenge.model.Quote
import kotlinx.android.synthetic.main.currency_rate_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class CurrencyRatesAdapter(var context: Context) :
    RecyclerView.Adapter<CurrencyRatesAdapter.ViewHolder>() {
    var ratesArray: ArrayList<Quote> = ArrayList()
    var reference: Double = 1.0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.currency_rate_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = ratesArray.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rate = ratesArray.get(position)
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING

        if (rate.updatedPrice == 0.0) {
            holder.itemView.currencyRate.text = df.format(rate.usdprice).toString()
        } else {
            holder.itemView.currencyRate.text = df.format(rate.updatedPrice).toString()
        }
        holder.itemView.currencyCountry.text = rate.country
    }

    fun updateList(quotes: ArrayList<Quote>) {
        ratesArray = quotes
        notifyDataSetChanged()
    }

    fun updateReference(selectedItem: String, num: Double) {
        ratesArray.forEach { quote ->
            if (quote.country.equals(selectedItem)) {
                reference = 1 / quote.usdprice
            }
        }
        ratesArray.forEach { quote ->
            quote.updatedPrice = quote.usdprice * num * reference
        }
        notifyDataSetChanged()
    }
}