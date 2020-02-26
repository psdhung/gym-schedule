package dave.gymschedule.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import dagger.android.AndroidInjection
import dave.gymschedule.R
import dave.gymschedule.common.model.Resource
import dave.gymschedule.schedule.presenter.GymSchedulePresenter
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import java.util.Calendar
import javax.inject.Inject

class GymScheduleWidget : AppWidgetProvider() {

    companion object {
        private const val TAG = "GymScheduleWidget"
    }

    @Inject
    lateinit var gymSchedulePresenter: GymSchedulePresenter

    private val disposables = CompositeDisposable()

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)
        super.onReceive(context, intent)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.gym_schedule_widget)

        disposables.add(gymSchedulePresenter.getGymEventsForDate(Calendar.getInstance())
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe({ visibleEventsResource ->
                    Log.d(TAG, "status: ${visibleEventsResource.status}")
                    when (visibleEventsResource.status) {
                        Resource.Status.LOADING -> {

                        }
                        Resource.Status.ERROR -> {
//                            showErrorMessage(context.getString(R.string.error_schedule_retrieval_failed), visibleEventsResource.error)
                        }
                        else -> {
                            val visibleEvents = visibleEventsResource.data
                            if (visibleEvents.isEmpty()) {
//                                showErrorMessage(context.getString(R.string.error_no_events))
                            } else {
                                Log.d(TAG, "successfully got schedule")

                            }
                        }
                    }
                }, { error ->
                    Log.d(TAG, "failed to retrieve schedule", error)
//                    showErrorMessage(context.getString(R.string.error_schedule_retrieval_failed), error)
                }))

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}