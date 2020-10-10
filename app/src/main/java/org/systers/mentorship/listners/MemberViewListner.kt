package org.systers.mentorship.listners

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.systers.mentorship.view.fragments.AdminFragment
import org.systers.mentorship.view.fragments.ManeteeFragment
import org.systers.mentorship.view.fragments.MentorFragment

internal class MemberViewListner(
        var context: Context,
        fm: FragmentManager,
        var totalTabs: Int
) :
        FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                MentorFragment()
            }
            1 -> {
                ManeteeFragment()
            }
            2 -> {
                AdminFragment()
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}