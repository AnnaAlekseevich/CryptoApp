package com.test.cryptoapp.ui.activities

import com.test.cryptoapp.net.models.Coin

interface CoinListItemClickListener {
    fun onCoinClicked(coin: Coin?)
}