package com.hhade.loans.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.hhade.loans.*
import kotlinx.android.synthetic.main.filter_view.*
import kotlinx.android.synthetic.main.filter_view.view.*
import kotlinx.android.synthetic.main.fragment_loans.*
import kotlinx.android.synthetic.main.fragment_loans.view.*

class LoansFragment : Fragment(), CompoundButton.OnCheckedChangeListener {

    var offers = ArrayList<Offers>()
    private val initialOffers = ArrayList<Offers>()
    private var adapter: RecyclerViewOffersAdapter? = null

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (buttonView.id == rating_sorting_switch.id) {

        } else if (buttonView.id == sum_sorting_switch.id) {
            if (isChecked)
                offers = Controller.sortOffersForSum(offers)
            else {
                offers.clear()
                for (offer in initialOffers)
                    offers.add(offer)
            }
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_loans, container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden)
            Controller.hideFilterState(this.loans_filter_layout)
    }

    fun initAdapter(offersList: ArrayList<Offers>, urlAllOffers: String) {
        var offers = offersList

        if (offers.size > 0) {
            adapter = RecyclerViewOffersAdapter(offers, urlAllOffers)

            for (offer in offers)
                initialOffers.add(offer)

            view!!.loans_list_view.layoutManager = LinearLayoutManager(activity)
            view!!.loans_list_view.adapter = adapter
        } else
            view!!.no_connection_text.visibility = View.VISIBLE

        view!!.loans_filter_button.setOnClickListener {
            Controller.changeFilterState(view!!.loans_filter_layout)
        }

        val onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { switch, isChecked ->
            if (isChecked) {
                if (switch.id == view!!.loans_filter_layout.sum_sorting_switch.id) {
                    offers = Controller.sortOffersForSum(offers)
                } else if (switch.id == view!!.loans_filter_layout.rating_sorting_switch.id) {
                    offers = Controller.sortOffersForRating(offers)
                }
            } else {
                offers.clear()
                for (offer in initialOffers)
                    offers.add(offer)
                view!!.loans_filter_layout.rating_sorting_switch.isChecked = false
                view!!.loans_filter_layout.sum_sorting_switch.isChecked = false
            }
            adapter!!.notifyDataSetChanged()
        }

        view!!.loans_filter_layout.sum_sorting_switch.setOnCheckedChangeListener(onCheckedChangeListener)
        view!!.loans_filter_layout.rating_sorting_switch.setOnCheckedChangeListener(onCheckedChangeListener)
    }
}
