package ru.alxr.cardvalidator

const val UNKNOWN = "Bank name is not available"

data class CardInfo(
    val scheme: String?,
    val type: String?,
    val brand: String?,
    val prepaid: Boolean,
    val number: CardNumber? = null,
    val country: Country? = null,
    val bank: Bank? = null
) {
    override fun toString(): String {
        return "${getBankName()}\nScheme is $scheme/$brand"
    }

    private fun getBankName(): String {
        if (bank == null) return UNKNOWN
        return if (bank.name.isNullOrEmpty()) UNKNOWN else bank.name
    }
}

class CardNumber(
    val length: Int,
    val luhn: Boolean
)

class Country(
    val numeric: String?,
    val alpha2: String?,
    val name: String?,
    val emoji: String?,
    val currency: String?,
    val latitude: Double,
    val longitude: Double
)

class Bank(
    val name: String?,
    val url: String?,
    val phone: String?,
    val city: String?
)