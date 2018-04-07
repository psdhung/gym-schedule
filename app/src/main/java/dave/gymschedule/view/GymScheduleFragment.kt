package dave.gymschedule.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dave.gymschedule.GymEventAdapter
import dave.gymschedule.R
import dave.gymschedule.model.GymEvent
import dave.gymschedule.presenter.GymSchedulePresenter
import dave.gymschedule.presenter.GymSchedulePresenterImpl
import kotlinx.android.synthetic.main.fragment_schedule_list.*
import java.util.Calendar

class GymScheduleFragment : Fragment(), GymScheduleView {

    companion object {
        const val DATE = "date"
    }

    private lateinit var presenter: GymSchedulePresenter
    private lateinit var date: Calendar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        date = Calendar.getInstance()
        val bundle = arguments
        bundle?.let {
            date.timeInMillis = bundle.getLong(DATE)
        }
        return inflater.inflate(R.layout.fragment_schedule_list, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = GymSchedulePresenterImpl(this)

        gym_events_recycler_view.layoutManager = LinearLayoutManager(activity)
        gym_events_recycler_view.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        presenter.onViewCreated(date)
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
        presenter.onViewDestroyed()
        super.onDestroyView()
    }
}