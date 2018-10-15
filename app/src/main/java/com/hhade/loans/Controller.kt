package com.hhade.loans

import android.content.Intent
import android.os.Build
import android.support.v4.app.FragmentActivity
import android.text.method.ScrollingMovementMethod
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.offer_description.view.*
import kotlinx.android.synthetic.main.offers_list_item.view.*

object Controller {

    private var isFilterOpened = false
    private var isDescriptionOpened = false
    var sourceCod = ""

    fun openWebActivity(activity: FragmentActivity, url: String) {
        val intent = Intent(activity, WebActivity::class.java)
        intent.putExtra("url", url + sourceCod)
        if (!isDescriptionOpened && !isFilterOpened)
            activity.startActivity(intent)
    }

    fun hideFilterState(filter: View) {
        if (filter.visibility == View.VISIBLE)
            filter.visibility = View.INVISIBLE
        isFilterOpened = false
    }

    fun changeFilterState(filter: View) {
        if (!isDescriptionOpened) {
            if (filter.visibility == View.INVISIBLE) {
                filter.visibility = View.VISIBLE
                isFilterOpened = true
            } else {
                isFilterOpened = false
                filter.visibility = View.INVISIBLE
            }
        }
    }

    fun openDescriptionView(activity: FragmentActivity, intent: Intent, itemView: View,
                            descriptionMap: LinkedHashMap<String, String>) {
        if (isFilterOpened || isDescriptionOpened)
            return

        isDescriptionOpened = true
        activity.offer_description.visibility = View.VISIBLE
        activity.offer_description.description_close_button.setOnClickListener {
            isDescriptionOpened = false
            activity.offer_description.visibility = View.INVISIBLE
        }
        with(activity.offer_description) {
            description_title.text = itemView.offer_name.text
            description_sum.text = itemView.offer_sum.text
            description_time.text = itemView.offer_time.text
            description_rate.text = itemView.offer_rate.text
            description_icon.setImageDrawable(itemView.offer_icon.drawable)

            description_text.movementMethod = ScrollingMovementMethod()
            description_text.text = parseMapToDescription(descriptionMap)

            issue_button.setOnClickListener {
                activity.startActivity(intent)
            }
        }
    }

    private fun parseMapToDescription(map: LinkedHashMap<String, String>): String {
        var descriptionText = ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            map.forEach { t, u ->
                descriptionText += "$t\n\n$u\n\n"
            }
        }

        return descriptionText
    }

    fun sortOffersForRating(offers: ArrayList<Offers>): ArrayList<Offers> {
        for (out in offers.size - 1 downTo 1) {
            for (i in 0 until out) {
                if (offers[i].rating < offers[i + 1].rating) {
                    val dummy = offers[i]
                    offers[i] = offers[i + 1]
                    offers[i + 1] = dummy
                }
            }
        }
        return offers
    }

    fun sortOffersForSum(offers: ArrayList<Offers>): ArrayList<Offers> {
        try {
            for (out in offers.size - 1 downTo 1) {
                for (i in 0 until out) {
                    if (offers[i].sum.toInt() < offers[i + 1].sum.toInt()) {
                        val dummy = offers[i]
                        offers[i] = offers[i + 1]
                        offers[i + 1] = dummy
                    }
                }
            }
        } catch (e: Exception) {
        }

        return offers
    }
}