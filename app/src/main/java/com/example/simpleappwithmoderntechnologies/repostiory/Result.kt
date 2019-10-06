package com.example.simpleappwithmoderntechnologies.repostiory

sealed class Result<T> {

    data class Success<T>(val value: T) : Result<T>()

    data class Failure<T>(val failReason: FailReason) : Result<T>()
}

enum class FailReason {
    CONNECTION_ERROR,
    NOT_FOUND_BASE_CURRENCY,
    NOT_FOUND_QUOTE_CURRENCY,
    UNKNOWN
}