package com.hhade.loans

import org.json.JSONException
import org.json.JSONObject

const val LOANS = 0
const val CARDS = 1
const val OTHER = 2

class JsonParser(jsonString: String) {

    private var json: JSONObject = JSONObject(jsonString)

    val url: String
        get() {
            var urlAllOffer = ""
            if (json.has("urlAllOffer"))
                urlAllOffer = json.getString("urlAllOffer")
            return urlAllOffer
        }

    fun getOffers(view: Int): ArrayList<Offers> {
        val offers = ArrayList<Offers>()

        try {
            val jsonArray = json.getJSONArray(
                    when (view) {
                        LOANS -> "loansOffers"
                        CARDS -> "cardsOffers"
                        OTHER -> "otherOffers"
                        else -> throw JSONException("Json array not found")
                    }
            )

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