package com.test.cryptoapp.crypto

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.test.cryptoapp.MainActivity
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.CryptoActivityBinding


class CryptoActivity : AppCompatActivity() {
    private lateinit var binding: CryptoActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CryptoActivityBinding.inflate(layoutInflater)

        setUpTabs()
        setSupportActionBar(binding.toolbarCrypto)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    private fun setUpTabs() {
        binding.toolbarCrypto.setNavigationIcon(R.drawable.back_from_crypto_activity)
        binding.toolbarCrypto.setLogo(R.drawable.crypto_icon)
        binding.toolbarCrypto.setTitle(R.string.crypto_name)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                // todo: goto back activity from here
                val intent = Intent(this@CryptoActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun changeProgressBarVisibility(show: Boolean) {
        binding.progressBar.setVisibility(if (show) View.VISIBLE else View.GONE)
    }

}