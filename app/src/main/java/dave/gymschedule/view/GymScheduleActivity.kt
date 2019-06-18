package dave.gymschedule.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity
import dave.gymschedule.R
import dave.gymschedule.SchedulePagerAdapter
import kotlinx.android.synthetic.main.activity_gym_schedule.*
import java.util.Calendar

class GymScheduleActivity : DaggerAppCompatActivity() {

    companion object {
        private const val MAX_DAYS = 7
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_schedule)

        setSupportActionBar(findViewById(R.id.toolbar))

        schedule_pager.adapter = SchedulePagerAdapter(supportFragmentManager, Calendar.getInstance(), MAX_DAYS)
        schedule_tab_layout.setupWithViewPager(schedule_pager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_settings -> {
                val startSettingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(startSettingsIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
