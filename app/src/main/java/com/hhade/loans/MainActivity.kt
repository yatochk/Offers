package com.hhade.loans

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.hhade.loans.fragments.CardFragment
import com.hhade.loans.fragments.LoansFragment
import com.hhade.loans.fragments.OtherFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var jsonParser: JsonParser
    private lateinit var jsonTask: JsonTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    if (pendingDynamicLinkData != null) {
                        val deepLink = pendingDynamicLinkData.link
                        Controller.sourceCod = "/?source=" + deepLink.getQueryParameter("source")
                    }
                }
                .addOnFailureListener(this) { e -> Log.w("FirebaseFail", "getDynamicLink:onFailure", e) }

        /**
         * Initialising fragments
         */
        val loansFragment = LoansFragment()
        val cardsFragment = CardFragment()
        val otherFragment = OtherFragment()

        /**
         * Save the active fragment to disable on transition between fragments
         */
        var activeFragment: Fragment = loansFragment

        /**
         * Add all fragments to container for show and hide on transition between then
         */
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, loansFragment).commit()
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, cardsFragment).hide(cardsFragment).commit()
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, otherFragment).hide(otherFragment).commit()

        bottom_navigation.setOnNavigationItemSelectedListener {
            when {
                it.itemId == R.id.nav_cards -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(cardsFragment).commit()
                    activeFragment = cardsFragment
                }
                it.itemId == R.id.nav_loans -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(loansFragment).commit()
                    activeFragment = loansFragment
                }
                it.itemId == R.id.nav_other -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(otherFragment).commit()
                    activeFragment = otherFragment
                }
            }

            true
        }

        try {
            jsonParser = JsonParser(resources.openRawResource(R.raw.offers)
                    .bufferedReader().use { it.readText() })
        } catch (e: NullPointerException) {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_LONG).show()
        }

        jsonTask = JsonTask(loansFragment, cardsFragment, otherFragment, resources.openRawResource(R.raw.offers)
                .bufferedReader().use { it.readText() })
        jsonTask.execute()
    }
}
