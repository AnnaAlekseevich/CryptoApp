package com.test.cryptoapp.ui.activities

import android.view.View
import com.test.cryptoapp.domain.models.Coin

interface CoinListItemClickListener {
    fun onCoinClicked(coin: Coin?, itemTextView: View)
}