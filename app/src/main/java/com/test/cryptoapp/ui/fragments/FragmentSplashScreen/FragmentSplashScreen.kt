package com.test.cryptoapp.ui.fragments.FragmentSplashScreen

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.FragmentSplashScreenBinding

class FragmentSplashScreen: Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var rocketAnimation: AnimationDrawable
    private lateinit var navController: NavController

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
        Handler(Looper.getMainLooper()).postDelayed({
            navController
                .navigate(R.id.action_fragmentSplashScreen_to_fragmentCoinsList,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.fragmentSplashScreen,
                            true).build()
                )
        }, 2000)
        return binding.root
    }
}