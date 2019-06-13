package dave.gymschedule.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dave.gymschedule.SettingsActivity
import dave.gymschedule.view.GymScheduleActivity
import dave.gymschedule.view.GymScheduleFragment

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun bindGymScheduleActivity(): GymScheduleActivity

    @ContributesAndroidInjector
    abstract fun bindSettingsActivity(): SettingsActivity

    @ContributesAndroidInjector
    abstract fun bindGymScheduleFragment() : GymScheduleFragment

}