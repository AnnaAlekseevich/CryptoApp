package com.test.cryptoapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.cryptoapp.R
import com.test.cryptoapp.adapters.CoinsListAdapter
import com.test.cryptoapp.crypto.CoinListItemClickListener
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.coinslist_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCoinClicked(coin: Coin?) {
        coin?.let { sendData(it) }
        val action =
            FragmentCoinsListDirections
                .actionFragmentCoinsListToFragmentCoinDetails()
        navController.navigate(action)

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
            //coinsAdapter?.submitData(lifecycle, PagingData.empty())
//            coinsAdapter?.retry()
//            coinsAdapter?.notifyDataSetChanged()

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

    private fun sendData(coin: Coin) {
        //todo change it after checking sending data
        val bundle = Bundle()
        var id = coin.cryptoId
        var price = coin.currentPriceCoin
        bundle.putString("ID_KEY", id)
        bundle.putDouble("PRICE_KEY", price)

        val fragment2 = FragmentCoinDetails()
        fragment2.setArguments(bundle)

        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.content, fragment2)
            ?.commit()
    }

}