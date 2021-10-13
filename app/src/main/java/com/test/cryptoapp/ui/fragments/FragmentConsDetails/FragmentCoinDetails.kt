package com.test.cryptoapp.ui.fragments.FragmentConsDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.FragmentCoinDelailsBinding
import com.test.cryptoapp.net.factories.CoinDetailsFragmentViewModelFactory
import com.test.cryptoapp.net.Api

class FragmentCoinDetails: Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentCoinDelailsBinding
    private lateinit var coinFragmentViewModel: FragmentCoinDetailsViewModel
    private lateinit var currentText: TextView
    private var price: Float = 0.0F
    private var highPrice: Float = 0.0F
    private var lowPrice: Float = 0.0F
    private lateinit var idCoin: String
    private var days: String = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 1500
        }
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoinDelailsBinding.inflate(layoutInflater)
        //todo change it - use with
        currentText = binding.day1
        binding.day1.setOnClickListener(this)
        binding.days7.setOnClickListener(this)
        binding.days30.setOnClickListener(this)
        binding.days365.setOnClickListener(this)
        binding.all.setOnClickListener(this)
        idCoin = arguments?.getString("symbol")!!
        price = arguments?.getFloat("price")!!
        highPrice = arguments?.getFloat("highPrice")!!
        lowPrice = arguments?.getFloat("lowPrice")!!
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }
        sharedElementEnterTransition = TransitionInflater.from(this.context).inflateTransition(android.R.transition.move)
//        sharedElementReturnTransition = TransitionInflater.from(this.context).inflateTransition(android.R.transition.move)

        setupViewModel()
        setupData()
        Log.d("ListCoinsFromAPI", " " + setupData())
        setupView()
        setHasOptionsMenu(true)
        return binding.root
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

    private fun setupView() {
        lifecycleScope.launchWhenCreated {
            coinFragmentViewModel.reloadChart(days, idCoin)
        }
    }

    private fun setupData() {
        binding.priceText.text = price.toString()
        binding.highPrice.text = highPrice.toString()
        binding.lowPrice.text = lowPrice.toString()
    }

    private fun setupViewModel() {
        coinFragmentViewModel =
            ViewModelProvider(
                this,
                CoinDetailsFragmentViewModelFactory(Api.getApiService())
            )[FragmentCoinDetailsViewModel::class.java]
    }

    override fun onClick(v: View?) {
        val newTextView = when (v?.id) {
            R.id.day1 -> {
                days = resources.getString(R.string.point_1day)
                Log.d("DAY", "currentDays1 = " + days)
                binding.day1
            }
            R.id.days7 -> {
                days = resources.getString(R.string.point_7days)
                Log.d("DAY", "currentDays7 = " + days)
                binding.days7
            }
            R.id.days30 -> {
                days = resources.getString(R.string.point_30days)
                Log.d("DAY", "currentDays30 = " + days)
                binding.days30
            }
            R.id.days365 -> {
                days = resources.getString(R.string.point_365days)
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
    }

    private fun changeTextView(currentTextView: TextView, newText: TextView) {
        currentTextView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        currentTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.silver_sand))
        newText.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_time_chart)
        newText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        currentText = newText
    }

}