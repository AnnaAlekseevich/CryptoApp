package com.test.cryptoapp.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.ActivityMainBinding
import com.test.cryptoapp.domain.models.ChartPoints
import com.test.cryptoapp.domain.models.Coin
import com.test.cryptoapp.domain.models.UiState

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bottomNavigationView = binding.btnNav
        setContentView(binding.root)

    }

    override fun onStart() {
        //TODO !REFACTORING! Better to initialize all needed parameters in onCreate()
        super.onStart()
        navController = findNavController(this, R.id.nav_host_fragment)
        setUpTabs()
        hideToolbarBottomNavigationSplashScreen()
    }

    private fun setUpTabs() {
        val navController =
            findNavController(this, R.id.nav_host_fragment)//navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.btn_nav)
            .setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController)
                || super.onOptionsItemSelected(item)
    }

    private fun hideToolbarBottomNavigationSplashScreen(){
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.fragmentSplashScreen -> {
                    bottomNavigationView.visibility = View.GONE
                }
                R.id.fragmentCoinDetails -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

}