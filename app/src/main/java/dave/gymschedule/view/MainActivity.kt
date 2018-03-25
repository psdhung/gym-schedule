package dave.gymschedule.view

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import dave.gymschedule.BaseActivity
import dave.gymschedule.GymEventAdapter
import dave.gymschedule.GymScheduleApplication
import dave.gymschedule.GymScheduleExpandableListAdapter
import dave.gymschedule.model.EventType
import dave.gymschedule.model.GymEvent
import dave.gymschedule.R
import dave.gymschedule.presenter.GymSchedulePresenter
import dave.gymschedule.presenter.GymSchedulePresenterImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

open class MainActivity : BaseActivity(), GymScheduleView {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var presenter: GymSchedulePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GymScheduleApplication.graph.inject(this)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        presenter = GymSchedulePresenterImpl(this)

        prev_button.setOnClickListener { _ -> presenter.onPrevPressed() }
        today_button.setOnClickListener { _ -> presenter.onTodayPressed() }
        next_button.setOnClickListener { _ -> presenter.onNextPressed() }

        // TODO: placeholder values
        pool_expandable_list_view.setAdapter(GymScheduleExpandableListAdapter(this, presenter,
                EventType.POOL_ACTIVITIES, listOf("Aquafit", "Swim lessions", "drop-in time", "clubs and teams")))
        val screenWidth = windowManager.defaultDisplay.width
        pool_expandable_list_view.setIndicatorBoundsRelative(screenWidth - 100, screenWidth)

        gym_events_recycler_view.layoutManager = LinearLayoutManager(this)
        gym_events_recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        presenter.onViewCreated()
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

    override fun showLoadingIndicator() {
        loading_text.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        loading_text.visibility = View.INVISIBLE
    }

    override fun disableAllRefreshButtons() {
        prev_button.isEnabled = false
        today_button.isEnabled = false
        next_button.isEnabled = false
    }

    override fun enablePrevButton() {
        prev_button.isEnabled = true
    }

    override fun disablePrevButton() {
        prev_button.isEnabled = false
    }

    override fun enableTodayButton() {
        today_button.isEnabled = true
    }

    override fun enableNextButton() {
        next_button.isEnabled = true
    }

    override fun disableTodayButton() {
        today_button.isEnabled = false
    }

    override fun updateSchedule(gymEvents: List<GymEvent>) {
        Log.d(TAG, "update schedule")
        gym_events_recycler_view.adapter = GymEventAdapter(gymEvents)
    }

    override fun setDate(date: String) {
        date_text.text = date
    }

    override fun disableNextButton() {
        next_button.isEnabled = false
    }

    override fun showErrorMessage(errorMessage: String, error: Throwable) {
        error_text.text = String.format("%s\n\n%s", errorMessage, error.message)
        error_text.visibility = View.VISIBLE
    }

    override fun hideErrorMessage() {
        error_text.visibility = View.INVISIBLE
    }

}
