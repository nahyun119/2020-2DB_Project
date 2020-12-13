package com.example.dbproject.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.dbproject.ui.mypage.Tab.Tab1Fragment
import com.example.dbproject.ui.mypage.Tab.Tab2Fragment

class ViewPagerAdapter(manager: FragmentManager, private val tabCount:Int) : FragmentStatePagerAdapter(manager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{
                Tab1Fragment()
            }
            else-> Tab2Fragment()
        }
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "좋아요한 목록"
            else -> "대여한 목록"
        }
    }
    override fun getCount(): Int {
        return tabCount
    }

}