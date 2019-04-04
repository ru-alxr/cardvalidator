package ru.alxr.cardvalidator

data class Result(
    val isValid: Boolean,
    val cardInfo: CardInfo? = null,
    val firstCheckError: CardException? = null,
    val secondCheckError: Exception? = null
)