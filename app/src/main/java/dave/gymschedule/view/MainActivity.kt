package dave.gymschedule.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import dave.gymschedule.BaseActivity
import dave.gymschedule.GymScheduleApplication
import dave.gymschedule.GymScheduleExpandableListAdapter
import dave.gymschedule.R
import dave.gymschedule.SchedulePagerAdapter
import dave.gymschedule.model.EventType
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Calendar

open class MainActivity : BaseActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName

        private const val MAX_DAYS = 7
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GymScheduleApplication.graph.inject(this)

        setContentView(R.layout.activity_main)

        schedule_pager.adapter = SchedulePagerAdapter(supportFragmentManager, Calendar.getInstance(), MAX_DAYS)

        setUpActivityDropdown()
    }

    private fun setUpActivityDropdown() {
        // TODO: placeholder values
        pool_expandable_list_view.setAdapter(GymScheduleExpandableListAdapter(this,
                EventType.POOL_ACTIVITIES, listOf("Aquafit", "Swim lessions", "drop-in time", "clubs and teams")))
        val screenWidth = windowManager.defaultDisplay.width
        pool_expandable_list_view.setIndicatorBoundsRelative(screenWidth - 100, screenWidth)
    }

}
