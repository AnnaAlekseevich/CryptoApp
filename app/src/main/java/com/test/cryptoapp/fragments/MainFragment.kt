package com.test.cryptoapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.cryptoapp.crypto.CoinListItemClickListener
import com.test.cryptoapp.crypto.CryptoActivity
import com.test.cryptoapp.crypto.CryptoResultAdapter
import com.test.cryptoapp.databinding.FragmentMainBinding
import com.test.cryptoapp.models.Coin

class MainFragment : Fragment(), CoinListItemClickListener {
    private lateinit var binding: FragmentMainBinding
    private var coinsAdapter: CryptoResultAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater)
        coinsAdapter = CryptoResultAdapter(this)
        binding.cryptosRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.cryptosRecyclerView.adapter = coinsAdapter
        addCoins(generateSubData())
        return binding.root
    }

    private fun generateSubData():List<Coin> {
        val coinsStubList = mutableListOf<Coin>()
        val coin1 = Coin("bitcoin", "btc", "Bitcoin",
            "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579", 49487.0)
        val coin2 = Coin("ethereum", "eth", "Ethereum",
            "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880", 3396.78)
        val coin3 = Coin("cardano", "ada", "Cardano",
            "https://assets.coingecko.com/coins/images/975/large/cardano.png?1547034860", 2.22)
        val coin4 = Coin("tether", "usdt", "Tether",
            "https://assets.coingecko.com/coins/images/325/large/Tether-logo.png?1598003707", 1.0)
        val coin5 = Coin("binancecoin", "bnb", "Binance Coin",
            "https://assets.coingecko.com/coins/images/825/large/binance-coin-logo.png?1547034615", 432.63)
        val coin6 = Coin("solana", "sol", "Solana",
            "https://assets.coingecko.com/coins/images/4128/large/coinmarketcap-solana-200.png?1616489452", 167.42)
        val coin7 = Coin("ripple", "xrp", "XRP",
            "https://assets.coingecko.com/coins/images/44/large/xrp-symbol-white-128.png?1605778731", 1.05)
        coinsStubList.add(coin1)
        coinsStubList.add(coin2)
        coinsStubList.add(coin3)
        coinsStubList.add(coin4)
        coinsStubList.add(coin5)
        coinsStubList.add(coin6)
        coinsStubList.add(coin7)
        return coinsStubList
    }


    private fun addCoins(coins:List<Coin>){
        coinsAdapter?.addCoins(coins)
    }

    override fun onCoinClicked(coin: Coin?) {
        val intent = Intent(context, CryptoActivity::class.java)
        startActivity(intent)
    }


}