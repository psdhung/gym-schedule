package dave.gymschedule.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import dagger.android.AndroidInjection
import dave.gymschedule.R
import dave.gymschedule.settings.GymLocationInteractor
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class GymScheduleWidget : AppWidgetProvider() {

    companion object {
        private val TAG = GymScheduleWidget::class.java.simpleName
    }

    @Inject
    lateinit var gymLocationInteractor: GymLocationInteractor

    private val disposables = CompositeDisposable()

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)
        super.onReceive(context, intent)
        Log.d(TAG, "onReceive")

        if (context != null && intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, Integer.MIN_VALUE)
            if (widgetId != Int.MIN_VALUE) {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                updateAppWidget(context, appWidgetManager, widgetId)
                appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_gym_schedule_list)
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d(TAG, "onUpdate")
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        Log.d(TAG, "updating widget $appWidgetId")
        val intent = Intent(context, GymScheduleWidgetService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
        }

        val remoteViews = RemoteViews(context.packageName, R.layout.gym_schedule_widget).apply {
            setRemoteAdapter(R.id.widget_gym_schedule_list, intent)
            setEmptyView(R.id.widget_gym_schedule_list, R.id.widget_loading_text)
        }

        setUpRefreshButton(remoteViews, context, appWidgetId)
        updateGymName(remoteViews, context, appWidgetManager, appWidgetId)

//        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    private fun updateGymName(remoteViews: RemoteViews, context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        disposables.add(gymLocationInteractor.savedGymLocationObservable
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe {
                    val locationName = context.getString(it.locationName)
                    Log.d(TAG, "got gym location: $it, name=$locationName")
                    remoteViews.setTextViewText(R.id.widget_gym_location, locationName)
                    appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_gym_schedule_list)
                })
    }

    private fun setUpRefreshButton(remoteViews: RemoteViews, context: Context, appWidgetId: Int) {
        val refreshIntent = Intent(context, this::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.widget_refresh_button, pendingIntent)
    }

    override fun onDisabled(context: Context?) {
        disposables.clear()
        super.onDisabled(context)
    }

}