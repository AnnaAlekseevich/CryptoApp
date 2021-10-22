package com.test.cryptoapp.ui.fragments.coinslist

import android.view.View
import com.test.cryptoapp.domain.models.Coin

interface CoinListItemClickListener {
    fun onCoinClicked(coin: Coin?, itemTextView: View)
}