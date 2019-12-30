package dave.gymschedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import dave.gymschedule.view.GymScheduleFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SchedulePagerAdapter(fragmentManager: FragmentManager,
                           private val currentDay: Calendar,
                           private val numDays: Int) : FragmentPagerAdapter(fragmentManager) {

    companion object {
        private val DISPLAYED_DATE_FORMAT = SimpleDateFormat("EE MMM dd, yyyy", Locale.getDefault())
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
        return DISPLAYED_DATE_FORMAT.format(getDateForFragment(position))
    }

    private fun getDateForFragment(position: Int): Long {
        val current = currentDay.clone() as Calendar
        current.add(Calendar.DAY_OF_YEAR, position)
        return current.timeInMillis
    }

}