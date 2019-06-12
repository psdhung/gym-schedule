package dave.gymschedule.view

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerFragment
import dave.gymschedule.GymEventAdapter
import dave.gymschedule.R
import dave.gymschedule.model.GymEventViewModel
import dave.gymschedule.presenter.GymSchedulePresenter
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_schedule_list.*
import java.util.ArrayList
import java.util.Calendar
import javax.inject.Inject

class GymScheduleFragment : DaggerFragment() {

    companion object {
        private const val TAG = "GymScheduleFragment"
        const val DATE_KEY = "date"
    }

    @Inject
    lateinit var presenter: GymSchedulePresenter

    private lateinit var date: Calendar

    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        date = Calendar.getInstance()
        val bundle = arguments
        bundle?.let {
            date.timeInMillis = bundle.getLong(DATE_KEY)
        }
        return inflater.inflate(R.layout.fragment_schedule_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated for date ${date.time}")
        gym_events_recycler_view?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        gym_events_recycler_view?.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.set(10, 10, 10, 10)
            }
        })

        error_text?.visibility = View.INVISIBLE
        loading_text?.visibility = View.VISIBLE
        updateSchedule(ArrayList())
        disposables.add(presenter.getGymEventsForDate(date)
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe({ visibleEvents ->
                    Log.d(TAG, "got events for date ${date.time}, revealing page")
                    updateSchedule(visibleEvents)
                    hideLoadingIndicator()
                }, { error ->
                    Log.d(TAG, "failed to retrieve schedule", error)
                    hideLoadingIndicator()
                    showErrorMessage("Could not retrieve schedule", error)
                })
        )
    }

    private fun updateSchedule(gymEvents: List<GymEventViewModel>) {
        gym_events_recycler_view?.adapter = GymEventAdapter(gymEvents)
    }

    private fun showErrorMessage(errorMessage: String, error: Throwable) {
        error_text?.text = String.format("%s\n\n%s", errorMessage, error.message)
        error_text?.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        loading_text?.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}