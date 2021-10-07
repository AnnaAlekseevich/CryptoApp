package com.test.cryptoapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.cryptoapp.crypto.CoinListItemClickListener
import com.test.cryptoapp.crypto.CoinsListAdapter
import com.test.cryptoapp.crypto.CryptoActivity
import com.test.cryptoapp.databinding.FragmentMainBinding
import com.test.cryptoapp.factory.MainFragmentViewModelFactory
import com.test.cryptoapp.models.Coin
import com.test.cryptoapp.net.Api
import kotlinx.coroutines.flow.collectLatest

class MainFragment : Fragment(), CoinListItemClickListener {
    private lateinit var binding: FragmentMainBinding
    private lateinit var mainFragmentViewModel: MainFragmentViewModel

    private var coinsAdapter: CoinsListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater)
        coinsAdapter = CoinsListAdapter(this)

        setupViewModel()
        setupList()
        Log.d("ListCoinsFromAPI", " " + setupList())
        setupView()

        binding.swipeRefreshLayout.setOnRefreshListener {
            // Initialize a new Runnable
            // Update the text view text with a random number
            //coinsAdapter?.refresh()
            // Hide swipe to refresh icon animation
            binding.swipeRefreshLayout.isRefreshing = false
        }
        coinsAdapter!!.addLoadStateListener {
            if (it.refresh == LoadState.Loading) {
                changeProgressBarVisibility(true)
            } else {
                changeProgressBarVisibility(false)
            }
        }
        return binding.root
    }

    override fun onCoinClicked(coin: Coin?) {
        val intent = Intent(context, CryptoActivity::class.java)
        startActivity(intent)
    }

    private fun changeProgressBarVisibility(show: Boolean) {
        binding.progressBar.setVisibility(if (show) View.VISIBLE else View.GONE)
    }

    private fun setupView() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            mainFragmentViewModel.listData.collectLatest { pagingData ->
                coinsAdapter?.submitData(pagingData)
            }
        }
    }

    private fun setupList() {
        coinsAdapter = CoinsListAdapter(this)
        binding.cryptosRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = coinsAdapter
        }
    }

    private fun setupViewModel() {
        mainFragmentViewModel =
            ViewModelProvider(
                this,
                MainFragmentViewModelFactory(Api.getApiService())
            )[MainFragmentViewModel::class.java]
    }

}