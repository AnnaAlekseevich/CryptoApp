package com.test.cryptoapp

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.sort_popup_menu -> {
                    showSortPopupMenu()
                }
                R.id.save -> {
                    saveData()
                }
            }
            return@setOnMenuItemClickListener true
        }
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
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

    private fun showSortPopupMenu() {
        // setup the alert builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sort")
    // add a radio button list
        val sort = arrayOf(getResources().getString(R.string.by_price), getResources().getString(R.string.alphabetically))
        val checkedItem = 1 // by_price
        builder.setSingleChoiceItems(sort, checkedItem) { dialog, which ->
            // user checked an item
        }
    // add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, which ->
            // user clicked OK
        }
        builder.setNegativeButton("Cancel", null)
    // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun saveData(){
        //todo create saving data to DB
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

}