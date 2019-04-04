package ru.alxr.cardvalidator.impl

import org.json.JSONObject
import ru.alxr.cardvalidator.*

class DefaultCardInfoParser : ICardInfoParser {

    override fun parse(source: JSONObject): CardInfo {
        val scheme = source.optString("scheme")
        val type = source.optString("type")
        val brand = source.optString("brand")
        val prepaid = source.optBoolean("prepaid")
        var info = CardInfo(
            scheme = scheme,
            type = type,
            brand = brand,
            prepaid = prepaid
        )

        val number = source.optJSONObject("number")
        number?.apply {
            info = info.copy(
                number = CardNumber(
                    length = optInt("length"),
                    luhn = optBoolean("luhn")
                )
            )
        }
        val country = source.optJSONObject("country")
        country?.apply {
            info = info.copy(
                country = Country(
                    numeric = optString("numeric"),
                    name = optString("name"),
                    alpha2 = optString("alpha2"),
                    currency = optString("currency"),
                    emoji = optString("emoji"),
                    latitude = optDouble("latitude"),
                    longitude = optDouble("longitude")
                )
            )
        }
        val bank = source.optJSONObject("bank")
        bank?.apply {
            info = info.copy(
                bank = Bank(
                    name = optString("name"),
                    city = optString("city"),
                    phone = optString("phone"),
                    url = optString("url")
                )
            )
        }
        return info
    }

}