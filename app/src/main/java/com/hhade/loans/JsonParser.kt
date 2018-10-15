package com.hhade.loans

import org.json.JSONArray
import org.json.JSONObject

const val loans = 0
const val cards = 1
const val other = 2

class JsonParser(jsonString: String) {

    private var json: JSONObject = JSONObject(jsonString)

    fun getUrlAllOffers(): String {
        var urlAllOffer = ""
        if (json.has("urlAllOffer"))
            urlAllOffer = json.getString("urlAllOffer")

        return urlAllOffer
    }

    fun getOffers(view: Int): ArrayList<Offers> {
        val offers = ArrayList<Offers>()

        try {
            lateinit var jsonArray: JSONArray
            when (view) {
                loans -> jsonArray = json.getJSONArray("loansOffers")
                cards -> jsonArray = json.getJSONArray("cardsOffers")
                other -> jsonArray = json.getJSONArray("otherOffers")
            }

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                // Парсинг описания оффера из json
                val jsonDescriptionArray = jsonObject.getJSONArray("description")
                val descriptionMap = LinkedHashMap<String, String>()
                for (j in 0 until jsonDescriptionArray.length()) {
                    val descriptionJsonObject = jsonDescriptionArray.getJSONObject(j)
                    descriptionMap[descriptionJsonObject.getString("name")] =
                            descriptionJsonObject.getString("specification")
                }

                val newOffers = Offers(
                        jsonObject.getString("name"),
                        jsonObject.getString("rating").toFloat(),
                        jsonObject.getString("sum"),
                        jsonObject.getString("time"),
                        jsonObject.getString("rate"),
                        jsonObject.getString("url") + Controller.sourceCod,
                        descriptionMap,
                        jsonObject.getString("image")
                )
                offers.add(newOffers)
            }

            return offers
        } catch (e: Exception) {
        }

        return offers
    }
}