package com.test.cryptoapp.crypto

import com.test.cryptoapp.models.Coin

interface CoinListItemClickListener {
    fun onCoinClicked(coin: Coin?)
}