package com.test.cryptoapp.crypto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.ItemListCryptoBinding
import com.test.cryptoapp.models.Coin

class CryptoResultAdapter :
    RecyclerView.Adapter<CryptoResultAdapter.CryptoViewHolder>() {

    private val data = mutableListOf<Coin>()

    fun addCoins( coins : List<Coin>){
        data.addAll(coins)
        //todo notifyItemRangeInserted
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        return CryptoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_crypto, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.updateCoin(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CryptoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemListCryptoBinding.bind(view)

        fun updateCoin(coin: Coin) {
            binding.imageView.setImageURI(coin.urlItemCrypto?.toUri())
            binding.titleCoinName.text = coin.getName()
            binding.subTitleSymbolName.text = coin.getcryptoSymbol()
            binding.priceCoin.text = coin.getcurrentPriceCoin().toString()
            Glide
                .with(binding.imageView.context)
                .load(binding.imageView.setImageURI(coin.urlItemCrypto?.toUri()))
                .into(binding.imageView)
        }

    }

}