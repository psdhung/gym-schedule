package dave.gymschedule

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import dave.gymschedule.view.GymScheduleFragment
import java.util.Calendar

class SchedulePagerAdapter(fragmentManager: FragmentManager,
                           private val currentDay: Calendar,
                           private val numDays: Int) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        val fragment = GymScheduleFragment()
        val args = Bundle()
        args.putLong(GymScheduleFragment.DATE, getDateForFragment(position))
        fragment.arguments = args
        return fragment
    }

    override fun getCount(): Int {
        return numDays
    }

    private fun getDateForFragment(position: Int): Long {
        val current = currentDay.clone() as Calendar
        current.add(Calendar.DAY_OF_YEAR, position)
        return current.timeInMillis
    }

}