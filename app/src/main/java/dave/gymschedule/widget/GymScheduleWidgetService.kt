package dave.gymschedule.widget

import android.content.Intent
import android.widget.RemoteViewsService
import dagger.android.AndroidInjection
import dave.gymschedule.schedule.presenter.GymSchedulePresenter
import javax.inject.Inject

class GymScheduleWidgetService : RemoteViewsService() {

    @Inject
    lateinit var gymSchedulePresenter: GymSchedulePresenter

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return GymScheduleWidgetRemoteViewsFactory(applicationContext, gymSchedulePresenter)
    }

}
