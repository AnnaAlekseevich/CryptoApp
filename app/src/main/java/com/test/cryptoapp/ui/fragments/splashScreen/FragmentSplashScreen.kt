package com.test.cryptoapp.ui.fragments.splashScreen

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSplashScreen: Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var rocketAnimation: AnimationDrawable
    private lateinit var navController: NavController
    private val viewModelSplash : FragmentSplashScreenViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(layoutInflater)

        navController = NavHostFragment.findNavController(this)
        binding.splashImage.apply {
            setBackgroundResource(R.drawable.progress_bar)
            rocketAnimation = background as AnimationDrawable
            rocketAnimation.start()
        }
        setupObserver()
        return binding.root
    }

    private fun setupObserver() {
        lifecycleScope.launchWhenStarted {
            viewModelSplash.myUiStateCoins.collect { coinsState ->
                if (coinsState) {
                    navController
                        .navigate(
                            R.id.action_fragmentSplashScreen_to_fragmentCoinsList,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(
                                    R.id.fragmentSplashScreen,
                                    true
                                ).build()
                        )
                }
            }
        }
    }

}