package com.test.cryptoapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.cryptoapp.R
import com.test.cryptoapp.crypto.CoinListItemClickListener
import com.test.cryptoapp.crypto.CoinsListAdapter
import com.test.cryptoapp.crypto.CryptoActivity
import com.test.cryptoapp.databinding.FragmentMainBinding
import com.test.cryptoapp.factory.MainFragmentViewModelFactory
import com.test.cryptoapp.models.Coin
import com.test.cryptoapp.net.Api
import kotlinx.coroutines.flow.collectLatest


class MainFragment : Fragment(), CoinListItemClickListener, Toolbar.OnMenuItemClickListener {
    private lateinit var binding: FragmentMainBinding
    private lateinit var mainFragmentViewModel: MainFragmentViewModel
    private var coinsAdapter: CoinsListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater)
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

    override fun onResume() {
        super.onResume()
        val toolbar: Toolbar? = requireActivity().findViewById(R.id.toolbar)
        toolbar?.setOnMenuItemClickListener(this)
    }

    override fun onCoinClicked(coin: Coin?) {
        val intent = Intent(context, CryptoActivity::class.java)
        startActivity(intent)
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
            )[MainFragmentViewModel::class.java]
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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.sort_popup_menu -> {
                showSortPopupMenu()
                return true
            }
        }
        return false
    }


}