package com.hhade.loans

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_header.view.*
import kotlinx.android.synthetic.main.offers_list_item.view.*

const val TYPE_HEADER = 0
const val TYPE_ITEM = 1

class RecyclerViewOffersAdapter(val offers: ArrayList<Offers>, private val urlAllOffers: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when {
                viewType == TYPE_ITEM -> OffersViewHolder(
                        LayoutInflater.from(parent.context).inflate(R.layout.offers_list_item, parent,
                                false)
                )
                viewType == TYPE_HEADER && urlAllOffers != "" -> RecycleHeader(
                        LayoutInflater.from(parent.context).inflate(R.layout.list_header, parent,
                                false)
                )
                else -> throw RuntimeException("There is no type that matches the type $viewType + make sure your using types correctly")
            }

    override fun getItemViewType(position: Int): Int {
        if (isPositionHeader(position) && urlAllOffers != "")
            return TYPE_HEADER

        return TYPE_ITEM
    }

    override fun getItemCount(): Int = if (urlAllOffers != "") offers.size + 1 else offers.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OffersViewHolder || urlAllOffers == "") {
            val mHolder = holder as OffersViewHolder
            val itemView = mHolder.itemView
            val activity = itemView.context as FragmentActivity
            val intent = Intent(activity, WebActivity::class.java)
            val itemPosition = if (urlAllOffers != "") position - 1 else position
            intent.putExtra("url", offers[itemPosition].url)

            Picasso.with(activity).load(offers[itemPosition].imageUrl).into(itemView.offer_icon)
            itemView.offer_name.text = offers[itemPosition].name
            itemView.offer_rating.rating = offers[itemPosition].rating
            itemView.offer_sum.text = (activity.getString(R.string.offer_sum) + offers[itemPosition].sum)
            itemView.offer_time.text = (activity.getString(R.string.offer_time) + offers[itemPosition].time)
            itemView.offer_rate.text = (activity.getString(R.string.offer_rate) + offers[itemPosition].rate)

            itemView.setOnClickListener { _ ->
                Controller.openDescriptionView(activity, intent, itemView, offers[itemPosition].descriptionMap)
            }

            itemView.open_button.setOnClickListener {
                Controller.openWebActivity(activity, offers[itemPosition].url)
            }
        } else if (holder is RecycleHeader) {
            val mHolder: RecycleHeader = holder
            val itemView = mHolder.itemView
            val activity = itemView.context as FragmentActivity


            itemView.apply_all_button.setOnClickListener {
                Controller.openWebActivity(activity, urlAllOffers)
            }
        }
    }

    private fun isPositionHeader(position: Int): Boolean = position == 0

    class OffersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class RecycleHeader(itemView: View) : RecyclerView.ViewHolder(itemView)
}
