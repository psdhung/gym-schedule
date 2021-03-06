package dave.gymschedule.schedule.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SchedulePagerAdapter(fragmentManager: FragmentManager,
                           dateFormat: String,
                           private val currentDay: Calendar,
                           private val numDays: Int) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val scheduleDateFormat by lazy {
        SimpleDateFormat(dateFormat, Locale.getDefault())
    }

    override fun getItem(position: Int): Fragment {
        val fragment = GymScheduleFragment()
        val args = Bundle()
        args.putLong(GymScheduleFragment.SCHEDULE_DATE_KEY, getDateForFragment(position))
        fragment.arguments = args
        return fragment
    }

    override fun getCount(): Int {
        return numDays
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return scheduleDateFormat.format(getDateForFragment(position))
    }

    private fun getDateForFragment(position: Int): Long {
        val current = currentDay.clone() as Calendar
        current.add(Calendar.DAY_OF_YEAR, position)
        return current.timeInMillis
    }
}