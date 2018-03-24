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
import dave.gymschedule.GymScheduleApplicationImpl
import dave.gymschedule.Model.GymEvent
import dave.gymschedule.GymScheduleExpandableListAdapter
import dave.gymschedule.R
import dave.gymschedule.interactor.GymScheduleInteractorImpl
import dave.gymschedule.presenter.GymSchedulePresenter
import dave.gymschedule.presenter.GymSchedulePresenterImpl
import dave.gymschedule.transformer.GymEventTransformer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

open class MainActivity : BaseActivity(), GymScheduleView {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var presenter: GymSchedulePresenter

    @Inject
    lateinit var transformer: GymEventTransformer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GymScheduleApplicationImpl.graph.inject(this)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val interactor = GymScheduleInteractorImpl(ourApplication.requestQueue, transformer)
        presenter = GymSchedulePresenterImpl(this, interactor)

        prev_button.setOnClickListener { _ -> presenter.onPrevPressed() }
        today_button.setOnClickListener { _ -> presenter.onTodayPressed() }
        next_button.setOnClickListener { _ -> presenter.onNextPressed() }

        // TODO: placeholder values
        pool_expandable_list_view.setAdapter(GymScheduleExpandableListAdapter(this,
                "Pool Activities", listOf("Aquafit", "Swim lessions", "drop-in time", "clubs and teams")))
        val screenWidth = windowManager.defaultDisplay.width
        pool_expandable_list_view.setIndicatorBoundsRelative(screenWidth - 100, screenWidth)

        pool_schedule_recycler_view.layoutManager = LinearLayoutManager(this)
        pool_schedule_recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
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


    override fun onStart() {
        super.onStart()
        presenter.onViewStarted()
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
        pool_schedule_recycler_view.adapter = GymEventAdapter(gymEvents)
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
