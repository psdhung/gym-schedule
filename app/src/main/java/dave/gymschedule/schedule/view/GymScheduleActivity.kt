package dave.gymschedule.schedule.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity
import dave.gymschedule.R
import dave.gymschedule.settings.GymLocationInteractor
import dave.gymschedule.settings.view.SettingsActivity
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_gym_schedule.*
import java.util.Calendar
import javax.inject.Inject

class GymScheduleActivity : DaggerAppCompatActivity() {

    companion object {
        private val TAG = GymScheduleActivity::class.java.simpleName
        private const val MAX_DAYS = 7
    }

    @Inject
    lateinit var gymLocationInteractor: GymLocationInteractor

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_schedule)

        setSupportActionBar(findViewById(R.id.toolbar))

        disposables.add(gymLocationInteractor.savedGymLocationObservable
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe({ gymLocation ->
                    val gymLocationName = getString(gymLocation.locationName)
                    supportActionBar?.title = gymLocationName
                }, {
                    Log.d(TAG, "error while getting gym location name", it)
                    supportActionBar?.setTitle(R.string.error_gym_name_retrieval_failed)
                }))

        schedule_pager.adapter = SchedulePagerAdapter(supportFragmentManager, getString(R.string.schedule_date_format), Calendar.getInstance(), MAX_DAYS)
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
