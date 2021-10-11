package com.test.cryptoapp.ui.fragments

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
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.FragmentCoinDelailsBinding
import com.test.cryptoapp.factories.CoinDetailsFragmentViewModelFactory
import com.test.cryptoapp.net.Api

class FragmentCoinDetails: Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentCoinDelailsBinding
    private lateinit var coinFragmentViewModel: FragmentCoinDetailsViewModel
    private lateinit var currentText: TextView
    private var price: Double = 0.0
    private lateinit var idCoin: String
    private var days: String = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCoinDelailsBinding.inflate(layoutInflater)
        val bundle = this.arguments

        if (bundle != null) {
            // handle your code here.
            idCoin = bundle.getString("ID_KEY")!!
            price = bundle.getDouble("PICE_KEY")
        }

        currentText = binding.day1
        binding.day1.setOnClickListener(this)
        binding.days7.setOnClickListener(this)
        binding.days30.setOnClickListener(this)
        binding.days365.setOnClickListener(this)
        binding.all.setOnClickListener(this)

        setUpTabs()
        //setSupportActionBar(binding.toolbarCrypto)
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

//    private fun changeProgressBarVisibility(show: Boolean) {
//        binding.progressBar.setVisibility(if (show) View.VISIBLE else View.GONE)
//    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        return super.onCreateOptionsMenu(menu)
//    }

    private fun setUpTabs() {
        binding.toolbarCrypto.setNavigationIcon(R.drawable.back_from_crypto_activity)
        binding.toolbarCrypto.setLogo(R.drawable.crypto_icon)
        binding.toolbarCrypto.setTitle(R.string.crypto_name)
    }

    private fun setupView() {
        lifecycleScope.launchWhenCreated {
            coinFragmentViewModel.reloadChart(days, idCoin)
            Log.d("Points list", "reloadChart = " + coinFragmentViewModel.reloadChart(days, idCoin))
        }
    }

    private fun setupData() {
        binding.titlePrice.text = price.toString()

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
                binding.day1
            }
            R.id.days7 -> {
                days = resources.getString(R.string.point_7days)
                binding.days7
            }
            R.id.days30 -> {
                days = resources.getString(R.string.point_30days)
                binding.days30
            }
            R.id.days365 -> {
                days = resources.getString(R.string.point_365days)
                binding.days365
            }
            R.id.all -> {
                days = resources.getString(R.string.point_ALL)
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