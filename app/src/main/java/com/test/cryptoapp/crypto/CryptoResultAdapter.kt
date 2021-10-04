package com.test.cryptoapp.crypto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.ItemListCryptoBinding

class CryptoResultAdapter :
    RecyclerView.Adapter<CryptoResultAdapter.CryptoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        return CryptoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_crypto, parent, false)
        )
    }

    private fun setItem(binding: ItemListCryptoBinding) { //, cryptoResults: CryptoResult

        setSmallItem(binding) //, cryptoResults

    }

    private fun setSmallItem(
        binding: ItemListCryptoBinding
    ) {

        Glide
            .with(binding.imageView.context)
//            .load(localCrypto.resultUrl)
//            .into(binding.imageView)

        binding.imageView.setOnClickListener {

        }

    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        //setItem(holder.binding, items[position])
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class CryptoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         val binding = ItemListCryptoBinding.bind(view)
    }

}