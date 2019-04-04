package ru.alxr.cardvalidator

import org.json.JSONObject

interface ICardInfoParser {

    fun parse(source: JSONObject): CardInfo

}