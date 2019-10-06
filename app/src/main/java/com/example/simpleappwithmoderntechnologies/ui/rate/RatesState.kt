package com.example.simpleappwithmoderntechnologies.ui.rate

import com.example.simpleappwithmoderntechnologies.model.Rate
import com.example.simpleappwithmoderntechnologies.repostiory.FailReason

sealed class RatesState {
    private val consumedSet = mutableSetOf<String>()

    data class RateData(val rate: Rate) : RatesState()
    data class Error(val failReason: FailReason) : RatesState()

    fun consumedByMe(uniqueId: String) {
        consumedSet.add(uniqueId)
    }

    fun wasConsumedByMe(uniqueId: String) = consumedSet.contains(uniqueId)
}