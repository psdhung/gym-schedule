package dave.gymschedule.di

import dagger.Component
import dagger.android.AndroidInjector
import dave.gymschedule.GymScheduleApplication
import dave.gymschedule.GymScheduleApplicationImpl
import dave.gymschedule.view.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [GymScheduleModule::class])
interface GymScheduleComponent : AndroidInjector<GymScheduleApplication> {
    fun inject(application: GymScheduleApplicationImpl)

    fun inject(activity: MainActivity)
}