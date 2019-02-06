package dave.gymschedule.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dave.gymschedule.view.GymScheduleFragment
import dave.gymschedule.view.MainActivity

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity() : MainActivity

    @ContributesAndroidInjector
    abstract fun bindGymScheduleFragment() : GymScheduleFragment
}