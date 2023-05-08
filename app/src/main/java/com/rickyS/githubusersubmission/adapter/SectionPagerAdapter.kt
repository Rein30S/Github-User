package com.rickyS.githubusersubmission.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rickyS.githubusersubmission.FollowFragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username = ""

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_POSITION, position + 1)
            putString(FollowFragment.ARG_USERNAME, username)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}