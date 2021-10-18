package com.test.cryptoapp.ui.fragments.coinslist

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.FragmentCoinsListBinding
import com.test.cryptoapp.net.Api
import com.test.cryptoapp.net.factories.MainFragmentViewModelFactory
import com.test.cryptoapp.net.models.Coin
import com.test.cryptoapp.ui.activities.CoinListItemClickListener
import com.test.cryptoapp.ui.adapter.CoinsListAdapter
import kotlinx.coroutines.flow.collectLatest

class FragmentCoinsList : Fragment(), CoinListItemClickListener {
    private lateinit var binding: FragmentCoinsListBinding
    private lateinit var mainFragmentViewModel: FragmentCoinsListViewModel
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
        coinsAdapter = CoinsListAdapter(this)
        setupViewModel()
        setupList()
        setupView()
        coinsAdapter?.addLoadStateListener { loadStates ->
            binding.swipeRefreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading
            if(loadStates.refresh == LoadState.Loading){
                changeProgressBarVisibility(true)
            } else{
                changeProgressBarVisibility(false)
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            coinsAdapter?.refresh()
        }
        //setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.coinslist_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCoinClicked(coin: Coin?, priceText: View) {
        if (coin != null) {
            var id = coin?.cryptoId
            val marketCap = coin?.marketCap
            val currentPrice = coin?.currentPriceCoin
            val percentage = coin?.changePercentage
            val icon = coin?.urlItemCrypto
            val bundle: Bundle = Bundle(5)
                .apply {
                    putString("id", id)
                    putFloat("marketCap", marketCap.toFloat())
                    putFloat("currentPrice", currentPrice.toFloat())
                    putString("percentage", percentage)
                    putString("icon", icon)
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
            )[FragmentCoinsListViewModel::class.java]
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
            coinsAdapter?.refresh()
        }
        builder.setNegativeButton("Cancel", null)
        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.sort -> {
                showSortPopupMenu()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}