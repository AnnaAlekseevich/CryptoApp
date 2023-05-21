package com.test.cryptoapp.ui.fragments.coinslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.FragmentCoinsListBinding
import com.test.cryptoapp.domain.models.Coin
import com.test.cryptoapp.ui.adapter.CoinsListAdapter
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentCoinsList : Fragment(), CoinListItemClickListener {
    private lateinit var binding: FragmentCoinsListBinding

    private val mainFragmentViewModel: FragmentCoinsListViewModel by viewModel()
    private var coinsAdapter: CoinsListAdapter? = null
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoinsListBinding.inflate(layoutInflater)
        binding.toolbar.inflateMenu(R.menu.coinslist_menu)
        binding.toolbar.setOnMenuItemClickListener {item: MenuItem? ->
            when (item?.itemId) {
                R.id.sort -> {
                    showSortPopupMenu()
                }
            }
            true
        }
        binding.toolbar.setTitle(R.string.cryptocurrencies_title)
        coinsAdapter = CoinsListAdapter(this)
        setupList()

        lifecycleScope.launchWhenCreated {
            mainFragmentViewModel.listData.collectLatest { pagingData ->
                coinsAdapter?.submitData(pagingData)
            }
        }
        coinsAdapter?.addLoadStateListener { loadStates ->
            binding.swipeRefreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading
            if (loadStates.refresh == LoadState.Loading) {
                changeProgressBarVisibility(true)
            } else {
                changeProgressBarVisibility(false)
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            mainFragmentViewModel.onDataRefreshed()
            coinsAdapter?.refresh()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }

    override fun onCoinClicked(coin: Coin?, priceText: View) {
        if (coin != null) {
            var id = coin.cryptoId
            val marketCap = coin.marketCap
            val currentPrice = coin.currentPriceCoin
            val percentage = coin.changePercentage
            val icon = coin.urlItemCrypto
            val name = coin.cryptoName
            val bundle: Bundle = Bundle(6)
                .apply {
                    putString("id", id)
                    putFloat("marketCap", marketCap.toFloat())
                    putFloat("currentPrice", currentPrice.toFloat())
                    putString("percentage", percentage)
                    putString("icon", icon)
                    putString("name", name)
                }
            priceText.transitionName = "priceText"
            val perfectText = FragmentNavigatorExtras(
                priceText to "priceText"
            )
            findNavController().navigate(
                R.id.action_fragmentCoinsList_to_fragmentCoinDetails,
                bundle,
                null,
                perfectText
            )
        }
    }

    private fun changeProgressBarVisibility(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun setupList() {
        coinsAdapter = CoinsListAdapter(this)
        binding.cryptosRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = coinsAdapter
        }
    }

    private fun showSortPopupMenu() {
        var currentSort = "market_cap_desc"
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Sort")
        val sort = arrayOf(
            resources.getString(R.string.by_price),
            resources.getString(R.string.by_volume_price)
        )
        val checkedItem = 0 // by_price
        builder.setSingleChoiceItems(sort, checkedItem) { dialog, which ->
            currentSort = if (which == 0) {
                "market_cap_desc"
            } else {
                "volume_desc"
            }
        }
        // add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, which ->
            mainFragmentViewModel.sortBy(currentSort)
            mainFragmentViewModel.onDataRefreshed()
            coinsAdapter?.refresh()
        }
        builder.setNegativeButton("Cancel", null)
        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

}