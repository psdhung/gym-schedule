package dave.gymschedule.widget

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import dave.gymschedule.R
import dave.gymschedule.common.model.Resource
import dave.gymschedule.schedule.model.GymViewModel
import dave.gymschedule.schedule.presenter.GymSchedulePresenter
import io.reactivex.disposables.CompositeDisposable
import java.util.Calendar

class GymScheduleWidgetRemoteViewsFactory(private val context: Context,
                                          private val gymSchedulePresenter: GymSchedulePresenter) : RemoteViewsService.RemoteViewsFactory {

    companion object {
        private const val TAG = "WidgetViewsFactory"
    }

    private val disposables = CompositeDisposable()
    private var gymViewModel: GymViewModel? = null

    override fun onCreate() {
        Log.d(TAG, "onCreate")
    }

    override fun getViewAt(position: Int): RemoteViews {
        val event = gymViewModel?.events?.get(position)
        return RemoteViews(context.packageName, R.layout.gym_event_summary).apply {
            setTextViewText(R.id.event_name, event?.name)
            val details = event?.details
            if (details.isNullOrBlank()) {
                setViewVisibility(R.id.event_details, View.GONE)
            } else {
                setViewVisibility(R.id.event_details, View.VISIBLE)
                setTextViewText(R.id.event_details, details)
            }
            setTextViewText(R.id.event_location, event?.location)
            setTextViewText(R.id.event_start_time, event?.startTime)
            setTextViewText(R.id.event_end_time, event?.endTime)
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        disposables.clear()
    }

    override fun onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged")
        val gymViewModelResource = gymSchedulePresenter.getGymEventsForDate(Calendar.getInstance())
                .filter { it.status == Resource.Status.SUCCESS }
                .blockingFirst()
        Log.d(TAG, "successfully got schedule for location ${gymViewModelResource.data.location}")
        this.gymViewModel = gymViewModelResource.data
    }

    override fun getCount(): Int = gymViewModel?.events?.size ?: 0

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

    override fun getViewTypeCount(): Int = 1

}