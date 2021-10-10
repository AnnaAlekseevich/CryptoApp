package com.test.cryptoapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.cryptoapp.R
import com.test.cryptoapp.crypto.CoinActivity
import com.test.cryptoapp.crypto.CoinListItemClickListener
import com.test.cryptoapp.adapters.CoinsListAdapter
import com.test.cryptoapp.databinding.FragmentCoinsListBinding
import com.test.cryptoapp.factories.MainFragmentViewModelFactory
import com.test.cryptoapp.fragments.FragmentCoinsListViewModel
import com.test.cryptoapp.models.Coin
import com.test.cryptoapp.net.Api
import kotlinx.coroutines.flow.collectLatest


class FragmentCoinsList : Fragment(), CoinListItemClickListener {
    private lateinit var binding: FragmentCoinsListBinding
    private lateinit var mainFragmentViewModel: FragmentCoinsListViewModel
    private var coinsAdapter: CoinsListAdapter? = null
    private lateinit var navController: NavController
    private lateinit var toolBarNavigationUI: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCoinsListBinding.inflate(layoutInflater)
        coinsAdapter = CoinsListAdapter(this)
        navController = findNavController(this)
        //setUpToolbar()
        setupViewModel()
        setupList()
        Log.d("ListCoinsFromAPI", " " + setupList())
        setupView()

        binding.swipeRefreshLayout.setOnRefreshListener {
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
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.coinslist_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCoinClicked(coin: Coin?) {
        coin?.let { sendData(it) }
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
        // setup the alert builder
        var currentSort = "market_cap_desc"
        //volume_desc
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Sort")
        // add a radio button list
        val sort = arrayOf(
            resources.getString(R.string.by_price),
            resources.getString(R.string.by_volume_price)
        )
        val checkedItem = 0 // by_price
        builder.setSingleChoiceItems(sort, checkedItem) { dialog, which ->
            // user checked an item
            currentSort = if (which == 0) {
                "market_cap_desc"
            } else {
                "volume_desc"
            }

        }
        // add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, which ->
            mainFragmentViewModel.sortBy(currentSort)
            //адаптер очистился и начал грузить новые данные
            coinsAdapter?.submitData(lifecycle, PagingData.empty())

        }

        builder.setNegativeButton("Cancel", null)
        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.fragmentCoinsList -> {
                showSortPopupMenu()
                return true
            }
        }
        return false
    }

    private fun sendData(coin: Coin) {
        val intent = Intent(context, CoinActivity::class.java)
        //PACK DATA
        intent.putExtra("SENDER_KEY", "MainFragment")
        var id = coin.cryptoId
        var price = coin.currentPriceCoin
        intent.putExtra("ID_KEY", id)
        Log.d("ID_KEY", "send id = " + id)
        intent.putExtra("PRICE_KEY", price)
        startActivity(intent)
    }

}