package dave.gymschedule.schedule.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import dagger.android.support.DaggerAppCompatActivity
import dave.gymschedule.R
import dave.gymschedule.common.database.GymLocationRepository
import dave.gymschedule.settings.model.GymLocation
import dave.gymschedule.settings.view.SettingsActivity
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_gym_schedule.*
import java.util.Calendar
import javax.inject.Inject

class GymScheduleActivity : DaggerAppCompatActivity() {

    companion object {
        private const val TAG = "GymScheduleActivity"
        private const val MAX_DAYS = 7
    }

    @Inject
    lateinit var gymLocationRepository: GymLocationRepository

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_schedule)

        setSupportActionBar(findViewById(R.id.toolbar))

        disposables.add(gymLocationRepository.savedGymLocationIdObservable
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe({
                    val gymLocation = GymLocation.getGymLocationByLocationId(it)
                    val gymLocationName = getString(gymLocation.locationName)
                    supportActionBar?.title = gymLocationName
                }, {
                    Log.d(TAG, "error while getting gym location name", it)
                }))

        schedule_pager.adapter = SchedulePagerAdapter(supportFragmentManager, Calendar.getInstance(), MAX_DAYS)
        schedule_tab_layout.setupWithViewPager(schedule_pager)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
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
