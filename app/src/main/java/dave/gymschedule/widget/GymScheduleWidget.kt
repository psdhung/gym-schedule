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
        private val TAG = GymScheduleWidget::class.java.simpleName
    }

    @Inject
    lateinit var gymSchedulePresenter: GymSchedulePresenter

    private val disposables = CompositeDisposable()

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)
        super.onReceive(context, intent)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.gym_schedule_widget)

        disposables.add(gymSchedulePresenter.getGymEventsForDate(Calendar.getInstance())
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe({ gymViewModelResource ->
                    Log.d(TAG, "status: ${gymViewModelResource.status}")
                    views.setTextViewText(R.id.widget_gym_location, gymViewModelResource.data.location.name)
                    when (gymViewModelResource.status) {
                        Resource.Status.LOADING -> {}
                        Resource.Status.ERROR -> {
                            showErrorMessage()
                        }
                        else -> {
                            val gymEvents = gymViewModelResource.data.events
                            if (gymEvents.isEmpty()) {
                                showErrorMessage()
                            } else {
                                Log.d(TAG, "successfully got schedule")

                            }
                        }
                    }
                }, { error ->
                    Log.d(TAG, "failed to retrieve schedule", error)
                    showErrorMessage()
                }))

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun showErrorMessage() {

    }
}