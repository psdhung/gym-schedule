package dave.gymschedule

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import dave.gymschedule.di.DaggerAppComponent

class GymScheduleApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
                .application(this)
                .build()
    }

}
