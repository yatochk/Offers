package com.hhade.loans.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.hhade.loans.Controller
import com.hhade.loans.Offers
import com.hhade.loans.R
import com.hhade.loans.RecyclerViewOffersAdapter
import kotlinx.android.synthetic.main.filter_view.view.*
import kotlinx.android.synthetic.main.fragment_cards.*
import kotlinx.android.synthetic.main.fragment_cards.view.*

class CardFragment : Fragment() {

    var offers = ArrayList<Offers>()
    private val initialOffers = ArrayList<Offers>()
    private var adapter: RecyclerViewOffersAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_cards, container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden)
            Controller.hideFilterState(this.cards_filter_layout)
    }

    fun initAdapter(offersList: ArrayList<Offers>) {
        val urlAllOffer = ""
        var offers = offersList
        val view = view!!

        if (offers.size > 0) {
            adapter = RecyclerViewOffersAdapter(offers, urlAllOffer)

            for (offer in offers)
                initialOffers.add(offer)

            view.cards_list_view.layoutManager = LinearLayoutManager(activity)
            view.cards_list_view.adapter = adapter
        } else
            view.no_connection_text.visibility = View.VISIBLE

        view.cards_filter_button.setOnClickListener {
            Controller.changeFilterState(view.cards_filter_layout)
        }

        val onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { switch, isChecked ->
            if (isChecked) {
                if (switch.id == view.cards_filter_layout.sum_sorting_switch.id)
                    offers = Controller.sortOffersForSum(offers)
                else if (switch.id == view.cards_filter_layout.rating_sorting_switch.id)
                    offers = Controller.sortOffersForRating(offers)
            } else {
                offers.clear()
                for (offer in initialOffers)
                    offers.add(offer)
                view.cards_filter_layout.rating_sorting_switch.isChecked = false
                view.cards_filter_layout.sum_sorting_switch.isChecked = false
            }
            adapter!!.notifyDataSetChanged()
        }

        view.cards_filter_layout.sum_sorting_switch.setOnCheckedChangeListener(onCheckedChangeListener)
        view.cards_filter_layout.rating_sorting_switch.setOnCheckedChangeListener(onCheckedChangeListener)
    }
}
