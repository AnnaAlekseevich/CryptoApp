package com.test.cryptoapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.ItemListCryptoBinding
import com.test.cryptoapp.domain.models.Coin
import com.test.cryptoapp.ui.fragments.coinslist.CoinListItemClickListener

class CoinsListAdapter(val coinListItemClickListener: CoinListItemClickListener) :
    PagingDataAdapter<Coin, CoinsListAdapter.CoinsViewHolder>(DataDifferntiator) {

    inner class CoinsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemListCryptoBinding.bind(view)

        fun updateCoin(coin: Coin) {
            binding.imageView.setImageURI(coin.urlItemCrypto?.toUri())
            binding.titleCoinName.text = coin.cryptoName
            binding.subTitleSymbolName.text = coin.cryptoSymbol
            binding.priceText.text = coin.currentPriceCoin.toString()
            coin.urlItemCrypto?.toUri()?.let {
                Glide
                    .with(binding.imageView.context)
                    .load(it)
                    .into(binding.imageView)
            }
            binding.clItem.setOnClickListener {
                coinListItemClickListener.onCoinClicked(coin, binding.priceText)
            }
        }

    }
    override fun onBindViewHolder(holder: CoinsViewHolder, position: Int) {
        getItem(position)?.let { holder.updateCoin(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoinsViewHolder {
        return CoinsViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_list_crypto, parent, false)
        )
    }

    object DataDifferntiator : DiffUtil.ItemCallback<Coin>() {

        override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem.cryptoId == newItem.cryptoId
        }

        override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem == newItem
        }
    }

}