package com.example.simpleappwithmoderntechnologies.ui.rate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleappwithmoderntechnologies.model.Rate
import com.example.simpleappwithmoderntechnologies.repostiory.RatesRepository
import com.example.simpleappwithmoderntechnologies.repostiory.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RatesViewModel(private val ratesRepository: RatesRepository) : ViewModel() {
    val scheduledStandardTime = 60000L
    private val _ratesStateLIveData = MutableLiveData<RatesState>()
    private var job: Job? = null

    val ratesStateLiveData: LiveData<RatesState>
        get() = _ratesStateLIveData

    fun startFetchData() {
        job = viewModelScope.launch {
            fetchDataFromRepository()
            executePeriodically()
        }
    }

    fun isFetchingActive() = job?.isActive ?: false

    fun stopFetchData() {
        job?.cancel()
    }


    private suspend fun executePeriodically() {
        while (true) {
            delay(scheduledStandardTime)
            fetchDataFromRepository()
        }
    }

    private suspend fun fetchDataFromRepository() {
        when (val result = ratesRepository.getUsdPlnRate()) {
            is Result.Success -> {
                _ratesStateLIveData.value = RatesState.RateData(result.value)
            }
            is Result.Failure -> handleError(result)
        }
    }

    private fun handleError(failure: Result.Failure<Rate>) {
        _ratesStateLIveData.value = RatesState.Error(failure.failReason)
    }
}
