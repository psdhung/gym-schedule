package dave.gymschedule.view

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import dave.gymschedule.R
import dave.gymschedule.SchedulePagerAdapter
import kotlinx.android.synthetic.main.activity_gym_schedule.*
import java.util.Calendar

class GymScheduleActivity : DaggerAppCompatActivity() {

    companion object {
        private val TAG = GymScheduleActivity::class.java.simpleName

        private const val MAX_DAYS = 7
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_schedule)
        schedule_pager.adapter = SchedulePagerAdapter(supportFragmentManager, Calendar.getInstance(), MAX_DAYS)
        schedule_tab_layout.setupWithViewPager(schedule_pager)
    }

}
