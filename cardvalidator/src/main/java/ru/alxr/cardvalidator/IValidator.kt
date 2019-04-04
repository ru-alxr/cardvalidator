package ru.alxr.cardvalidator

interface IValidator {

    fun validate(source: String): Result

}