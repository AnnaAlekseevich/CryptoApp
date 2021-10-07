package com.test.cryptoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.test.cryptoapp.adapters.ViewPagerAdapter
import com.test.cryptoapp.databinding.ActivityMainBinding
import com.test.cryptoapp.fragments.MainFragment
import com.test.cryptoapp.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setUpTabs()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateToolbar(position)
            }
        })

        updateToolbar(0)
        setContentView(binding.root)
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(MainFragment(), "Main")
        adapter.addFragment(SettingsFragment(), "Settings")
        binding.viewPager.adapter = adapter

        TabLayoutMediator(
            binding.tabs, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.main_icon)
                    tab.text = getString(R.string.tab_main)
                }
                1 -> {
                    tab.setIcon(R.drawable.settings_icon)
                    tab.text = getString(R.string.tab_settings)
                }
            }
        }.attach()
    }

    private fun updateToolbar(position: Int) {
        binding.toolbar.menu.clear()
        when (position) {
            0 -> {
                binding.toolbar.setTitle(R.string.app_name)
                binding.toolbar.inflateMenu(R.menu.cryptocurrencies_menu)
            }
            1 -> {
                binding.toolbar.setTitle(R.string.settings)
                binding.toolbar.inflateMenu(R.menu.settings_menu)
            }
        }
    }



}