package dave.gymschedule.di

import dagger.Component
import dave.gymschedule.GymScheduleApplication
import dave.gymschedule.GymScheduleExpandableListAdapter
import dave.gymschedule.presenter.GymSchedulePresenterImpl
import dave.gymschedule.view.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface GymScheduleComponent {
    fun inject(application: GymScheduleApplication)

    fun inject(activity: MainActivity)

    fun inject(presenter: GymSchedulePresenterImpl)

    fun inject(gymScheduleExpandableListAdapter: GymScheduleExpandableListAdapter)
}