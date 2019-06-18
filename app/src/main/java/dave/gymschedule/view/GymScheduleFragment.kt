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
import dave.gymschedule.R
import dave.gymschedule.presenter.GymSchedulePresenter
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_schedule_list.*
import java.util.Calendar
import javax.inject.Inject

class GymScheduleFragment : DaggerFragment() {

    companion object {
        private const val TAG = "GymScheduleFragment"
        const val SCHEDULE_DATE_KEY = "schedule_date"
    }

    @Inject
    lateinit var presenter: GymSchedulePresenter

    private lateinit var date: Calendar

    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        date = Calendar.getInstance()
        arguments?.let {
            date.timeInMillis = it.getLong(SCHEDULE_DATE_KEY)
        }
        return inflater.inflate(R.layout.fragment_schedule_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated for date ${date.time}")
        val adapter = GymEventAdapter()
        gym_events_recycler_view?.adapter = adapter
        gym_events_recycler_view?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        gym_events_recycler_view?.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.set(30, 10, 30, 10)
            }
        })

        error_text?.visibility = View.INVISIBLE
        loading_text?.visibility = View.VISIBLE

        disposables.add(presenter.getGymEventsForDate(date)
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe({ visibleEvents ->
                    Log.d(TAG, "got events for date ${date.time}, revealing page")
                    adapter.gymEvents = visibleEvents
                    hideLoadingIndicator()
                }, { error ->
                    Log.d(TAG, "failed to retrieve schedule", error)
                    hideLoadingIndicator()
                    showErrorMessage("Could not retrieve schedule", error)
                })
        )
    }

    private fun hideLoadingIndicator() {
        loading_text?.visibility = View.INVISIBLE
    }

    private fun showErrorMessage(errorMessage: String, error: Throwable) {
        error_text?.text = String.format("%s\n\n%s", errorMessage, error.message)
        error_text?.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

}