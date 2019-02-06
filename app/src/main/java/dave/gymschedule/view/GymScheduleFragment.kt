package dave.gymschedule.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import dave.gymschedule.GymEventAdapter
import dave.gymschedule.R
import dave.gymschedule.model.GymEvent
import dave.gymschedule.presenter.GymSchedulePresenter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_schedule_list.*
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GymScheduleFragment : Fragment(), GymScheduleView {

    companion object {
        private const val TAG = "GymScheduleFragment"
        private val DISPLAYED_DATE_FORMAT = SimpleDateFormat("EEEE MMM dd, YYYY", Locale.getDefault())
        const val DATE_KEY = "date"
    }

    @Inject
    lateinit var presenter: GymSchedulePresenter

    private lateinit var date: Calendar

    private val disposables = CompositeDisposable()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        date = Calendar.getInstance()
        val bundle = arguments
        bundle?.let {
            date.timeInMillis = bundle.getLong(DATE_KEY)
        }
        return inflater.inflate(R.layout.fragment_schedule_list, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gym_events_recycler_view.layoutManager = LinearLayoutManager(activity)
        gym_events_recycler_view.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        hideErrorMessage()
        showLoadingIndicator()
        updateSchedule(ArrayList())
        setDate(DISPLAYED_DATE_FORMAT.format(date.time))
        disposables.add(presenter.getGymEventsForDate(date)
                .subscribe({ visibleEvents ->
                    updateSchedule(visibleEvents)
                    hideLoadingIndicator()
                }, { error ->
                    Log.d(TAG, "failed to retrieve schedule", error)
                    hideLoadingIndicator()
                    showErrorMessage("Could not retrieve schedule", error)
                })
        )
    }

    override fun setDate(date: String) {
        date_text.text = date
    }

    override fun updateSchedule(gymEvents: List<GymEvent>) {
        gym_events_recycler_view.adapter = GymEventAdapter(gymEvents)
    }

    override fun showErrorMessage(errorMessage: String, error: Throwable) {
        error_text.text = String.format("%s\n\n%s", errorMessage, error.message)
        error_text.visibility = View.VISIBLE
    }

    override fun hideErrorMessage() {
        error_text.visibility = View.INVISIBLE
    }

    override fun showLoadingIndicator() {
        loading_text.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        loading_text.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        disposables.dispose()
        super.onDestroyView()
    }
}