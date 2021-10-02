package com.test.cryptoapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.cryptoapp.fragments.MainFragment
import com.test.cryptoapp.fragments.SettingsFragment
import java.util.*


class ViewPagerAdapter(activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    private val mFragmentList : MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList : MutableList<String> = ArrayList()


    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Main"
            1 -> "Settings"
            else -> "Main"
        }
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return MainFragment()
            1 -> return SettingsFragment()

        }
        return MainFragment()
    }


}