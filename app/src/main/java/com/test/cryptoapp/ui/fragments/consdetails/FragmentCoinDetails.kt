package com.test.cryptoapp.ui.fragments.consdetails

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.FragmentCoinDelailsBinding
import com.test.cryptoapp.net.Api
import com.test.cryptoapp.net.factories.CoinDetailsFragmentViewModelFactory
import com.test.cryptoapp.net.models.ChartPoints
import com.test.cryptoapp.net.models.Coin
import com.test.cryptoapp.net.models.UiState
import com.test.cryptoapp.ui.activities.OnFragmentInteractionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect


class FragmentCoinDetails : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentCoinDelailsBinding
    private lateinit var coinFragmentViewModel: FragmentCoinDetailsViewModel
    private lateinit var currentText: TextView
    private var marketCapCoin: Float = 0.0F
    private var currentPriceCoin: Float = 0.0F
    private lateinit var idCoin: String
    private lateinit var percentage: String
    private lateinit var icon: String
    private var days: String = ""

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 15000
        }
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCoinDelailsBinding.inflate(layoutInflater)

        currentText = binding.day1

        binding.day1.setOnClickListener(this)
        binding.days7.setOnClickListener(this)
        binding.days30.setOnClickListener(this)
        binding.days365.setOnClickListener(this)
        binding.all.setOnClickListener(this)

        idCoin = arguments?.getString("id")!!
        marketCapCoin = arguments?.getFloat("marketCap")!!
        percentage = arguments?.getString("percentage")!!
        currentPriceCoin = arguments?.getFloat("currentPrice")!!
        icon = arguments?.getString("icon")!!
        Log.d("arguments", "idCoin = " + idCoin)
        Log.d("arguments", "price = " + marketCapCoin)
        var coin = Coin(
            cryptoId = idCoin,
            currentPriceCoin = currentPriceCoin.toDouble(),
            cryptoName = "",
            cryptoSymbol = "",
            urlItemCrypto = "",
            changePercentage = percentage,
            marketCap = marketCapCoin.toDouble()
        )

        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }
        sharedElementEnterTransition =
            TransitionInflater.from(this.context).inflateTransition(android.R.transition.move)
        if (mListener != null) {
            mListener!!.onFragmentInteraction(idCoin)
        }
        setupViewModel()
        showDataCoin(coin)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.actionBar?.title = idCoin
        //activity?.actionBar?.setLogo(R.drawable.small_icon_crypto)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity: Activity?
        if (context is Activity) {
            activity = context
            mListener = try {
                activity as OnFragmentInteractionListener
            } catch (e: ClassCastException) {
                throw ClassCastException("$activity must implement OnFragmentInteractionListener")
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                //onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun showDataCoin(coin: Coin) {
        binding.priceText.text = String.format("%.2f", coin.currentPriceCoin) + " $"
        binding.marketPricePercentage.text = coin.changePercentage + " %"
        binding.marketCap.text = "$ " + String.format("%.2f", coin.marketCap)
    }

    private fun showDataChart(points: ChartPoints) {

    }

    private fun setupViewModel() {
        coinFragmentViewModel =
            ViewModelProvider(
                this,
                CoinDetailsFragmentViewModelFactory(Api.getApiService(), Dispatchers.IO)
            )[FragmentCoinDetailsViewModel::class.java]

        lifecycleScope.launchWhenStarted {
            coinFragmentViewModel.myUiState.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> changeProgressBarVisibility(true)
                    is UiState.Success<Pair<Coin, ChartPoints>> -> {
                        Log.d("UiState.Success", "ChartPoints  = " + uiState.data.first)

                        changeProgressBarVisibility(false)
                        showDataCoin(uiState.data.first)
                        showDataChart(uiState.data.second)
                    }
                    is UiState.Error -> {
                        showErrorMessage(uiState.error)
                        changeProgressBarVisibility(false)
                    }

                }
            }
        }
    }

    override fun onClick(v: View?) {
        val newTextView = when (v?.id) {
            R.id.day1 -> {
                days = resources.getString(R.string.point_1day)
                percentage = resources.getString(R.string.point_chart_24H)
                Log.d("DAY", "currentDays1 = " + days)
                binding.day1
            }
            R.id.days7 -> {
                days = resources.getString(R.string.point_7days)
                percentage = resources.getString(R.string.point_chart_7d)
                Log.d("DAY", "currentDays7 = " + days)
                binding.days7
            }
            R.id.days30 -> {
                days = resources.getString(R.string.point_30days)
                percentage = resources.getString(R.string.point_chart_30d)
                Log.d("DAY", "currentDays30 = " + days)
                binding.days30
            }
            R.id.days365 -> {
                days = resources.getString(R.string.point_365days)
                percentage = resources.getString(R.string.point_chart_200d)
                Log.d("DAY", "currentDays365 = " + days)
                binding.days365
            }
            R.id.all -> {
                days = resources.getString(R.string.point_ALL)
                Log.d("DAY", "currentDaysALL = " + days)
                binding.all
            }
            else -> days
        }
        (newTextView as? TextView)?.let { changeTextView(currentText, it) }
        Log.d("FLOW", "Fragment coin details ")
        coinFragmentViewModel.requestsForCoinDetails(idCoin, days, percentage)
    }

    private fun changeTextView(currentTextView: TextView, newText: TextView) {
        currentTextView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        currentTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.silver_sand))
        newText.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.background_time_chart)
        newText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        currentText = newText
    }

    private fun showErrorMessage(exception: String) {
        view?.let {
            val snack = Snackbar.make(it, exception, Snackbar.LENGTH_LONG)
            snack.show()
        }
    }

    private fun changeProgressBarVisibility(show: Boolean) {
        binding.progressBar.isVisible = show
    }

}