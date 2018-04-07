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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GymScheduleApplication.graph.inject(this)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        schedule_pager.adapter = SchedulePagerAdapter(supportFragmentManager, Calendar.getInstance(), 7)

        setUpActivityDropdown()
    }

    private fun setUpActivityDropdown() {
        // TODO: placeholder values
        pool_expandable_list_view.setAdapter(GymScheduleExpandableListAdapter(this,
                EventType.POOL_ACTIVITIES, listOf("Aquafit", "Swim lessions", "drop-in time", "clubs and teams")))
        val screenWidth = windowManager.defaultDisplay.width
        pool_expandable_list_view.setIndicatorBoundsRelative(screenWidth - 100, screenWidth)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}
