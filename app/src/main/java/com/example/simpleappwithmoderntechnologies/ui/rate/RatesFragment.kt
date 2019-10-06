package com.example.simpleappwithmoderntechnologies.ui.rate

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.simpleappwithmoderntechnologies.R
import com.example.simpleappwithmoderntechnologies.repostiory.FailReason
import kotlinx.android.synthetic.main.ratest_fragment.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.RoundingMode


class RatesFragment : Fragment() {

    val UNIQUE_ID = javaClass.name

    private val viewModel: RatesViewModel by viewModel()
    private val newValueAppearTransitionTime = 1000
    private val reverseTransitionTime = 300

    companion object {
        fun newInstance() = RatesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ratest_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rateTv.setText(R.string.fetching)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeOnEvents()
    }

    override fun onStart() {
        super.onStart()
        if (!viewModel.isFetchingActive()) {
            viewModel.startFetchData()
        }
    }

    override fun onStop() {
        super.onStop()
        if (!requireActivity().isChangingConfigurations) {
            viewModel.stopFetchData()
        }
    }

    private fun subscribeOnEvents() {
        viewModel.ratesStateLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { rateState ->
                when (rateState) {
                    is RatesState.RateData -> {
                        if (rateState.wasConsumedByMe(UNIQUE_ID)) {
                            insertRate(rateState)
                        } else {
                            insertRateWithAnim(rateState)
                        }
                    }
                    is RatesState.Error -> handleError(rateState.failReason)
                }
                rateState.consumedByMe(UNIQUE_ID)
            }
        })
    }

    private fun handleError(failReason: FailReason) {
        Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT).show()
    }

    private fun insertRateWithAnim(data: RatesState.RateData) {
        insertRate(data)
        val transition = rateTv.background as TransitionDrawable
        transition.startTransition(newValueAppearTransitionTime)
        lifecycleScope.launch {
            scheduleReverseTransition(
                transition,
                newValueAppearTransitionTime,
                reverseTransitionTime
            )
        }
    }

    private fun insertRate(data: RatesState.RateData) {
        rateTv.text = data.rate.price.setScale(4, RoundingMode.CEILING).toPlainString()
    }

    private suspend fun scheduleReverseTransition(
        transition: TransitionDrawable,
        scheduleTime: Int,
        reverseTransitionTime: Int
    ) {
        delay(scheduleTime.toLong())
        transition.reverseTransition(reverseTransitionTime)
    }

}
