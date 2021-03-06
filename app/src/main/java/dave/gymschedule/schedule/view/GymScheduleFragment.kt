package dave.gymschedule.schedule.view

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
import dave.gymschedule.common.model.Resource
import dave.gymschedule.schedule.presenter.GymSchedulePresenter
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_gym_schedule.*
import java.util.Calendar
import javax.inject.Inject

class GymScheduleFragment : DaggerFragment() {

    companion object {
        private val TAG = GymScheduleFragment::class.java.simpleName
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
        return inflater.inflate(R.layout.fragment_gym_schedule, container, false)
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
                .subscribe({ gymViewModel ->
                    when (gymViewModel.status) {
                        Resource.Status.LOADING -> {
                            hideGymEvents()
                            hideErrorMessage()
                            showLoadingIndicator()
                        }
                        Resource.Status.ERROR -> {
                            hideGymEvents()
                            hideLoadingIndicator()
                            showErrorMessage(getString(R.string.error_schedule_retrieval_failed), gymViewModel.error)
                        }
                        else -> {
                            Log.d(TAG, "got events for date ${date.time}, revealing page")
                            val visibleEvents = gymViewModel.data.events
                            if (visibleEvents.isEmpty()) {
                                hideLoadingIndicator()
                                hideGymEvents()
                                showErrorMessage(getString(R.string.error_no_events))
                            } else {
                                hideLoadingIndicator()
                                hideErrorMessage()
                                adapter.gymEvents = visibleEvents
                                gym_events_recycler_view?.visibility = View.VISIBLE
                            }
                        }
                    }
                }, { error ->
                    Log.d(TAG, "failed to retrieve schedule", error)
                    hideGymEvents()
                    hideLoadingIndicator()
                    showErrorMessage(getString(R.string.error_schedule_retrieval_failed), error)
                })
        )
    }

    private fun hideGymEvents() {
        gym_events_recycler_view?.visibility = View.INVISIBLE
    }

    private fun showLoadingIndicator() {
        loading_text?.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        loading_text?.visibility = View.INVISIBLE
    }

    private fun showErrorMessage(errorMessage: String, error: Throwable? = null) {
        if (error == null) {
            error_text?.text = errorMessage
        } else {
            error_text?.text = String.format("%s\n\n%s", errorMessage, error.message)
        }
        error_text?.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        error_text?.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

}