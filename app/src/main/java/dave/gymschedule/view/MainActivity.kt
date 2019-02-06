package dave.gymschedule.view

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import dave.gymschedule.R
import dave.gymschedule.SchedulePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Calendar

open class MainActivity : DaggerAppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName

        private const val MAX_DAYS = 7
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        schedule_pager.adapter = SchedulePagerAdapter(supportFragmentManager, Calendar.getInstance(), MAX_DAYS)
    }

}
