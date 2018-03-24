package dave.gymschedule.di

import dagger.Component
import dave.gymschedule.GymScheduleApplication
import dave.gymschedule.view.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [GymScheduleModule::class])
interface GymScheduleComponent {
    fun inject(application: GymScheduleApplication)

    fun inject(activity: MainActivity)
}