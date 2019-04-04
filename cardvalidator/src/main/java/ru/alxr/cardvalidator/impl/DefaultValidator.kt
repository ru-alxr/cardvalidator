package ru.alxr.cardvalidator.impl

import org.json.JSONObject
import ru.alxr.cardvalidator.CardException
import ru.alxr.cardvalidator.ICardInfoParser
import ru.alxr.cardvalidator.IValidator
import ru.alxr.cardvalidator.Result
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

const val URL = "https://lookup.binlist.net/"

class DefaultValidator(private val parser: ICardInfoParser) : IValidator {

    override fun validate(source: String): Result {
        val result: Result =
            try {
                commonCheck(source)
                Result(true)
            } catch (e: CardException) {
                Result(false, firstCheckError = e)
            }
        if (source.trim().isEmpty()) return result
        val json: JSONObject
        return try {
            json = getCardDescription(source)
            result.copy(cardInfo = parser.parse(json))
        } catch (e: Exception) {
            // todo: здесь есть вопрос, что возвращать,
            // todo: в ТЗ не сказано что делать в случае проблем с
            // todo: сервисом получения информации о карте или со связью вообще
            return result.copy(secondCheckError = e)
        }
    }

    @Throws(CardException::class)
    fun commonCheck(source: String) {
        if (source.length !in 12..19) throw CardException("Card number must be 12 to 19 symbols long (${source.length})")
        source.map {
            if (!it.isDigit()) throw CardException("Wrong source: $source")
        }
        if (source.startsWith('0')) throw CardException("Card number cannot start with zero")
        if (!luhnAlgorithm(source)) throw CardException("Luhn check fail with $source")
    }

    private fun luhnAlgorithm(cardNumber: String): Boolean {
        val digits = cardNumber.map { Character.getNumericValue(it) }.toMutableList()
        for (i in (digits.size - 2) downTo 0 step 2) {
            var value = digits[i] * 2
            if (value > 9) {
                value = value % 10 + 1
            }
            digits[i] = value
        }
        return digits.sum() % 10 == 0
    }

    private fun getCardDescription(cardNumber: String): JSONObject {
        val url: URL
        try {
            url = URL("$URL${cardNumber.substring(0..5)}")
        } catch (e: MalformedURLException) {
            return JSONObject()
        }
        val connection: HttpURLConnection
        connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("Accept-Version", "3")
        connection.setRequestProperty("Content-Type", "application/json")
        connection.requestMethod = "GET"
        connection.connect()
        val reader: Reader = InputStreamReader(connection.inputStream)
        val bufferedReader = BufferedReader(reader)
        val stringBuilder = StringBuilder()
        while (true) {
            val line: String = bufferedReader.readLine() ?: break
            stringBuilder.append(line + "\n")
        }
        bufferedReader.close()
        connection.disconnect()
        return JSONObject(stringBuilder.toString())
    }

}