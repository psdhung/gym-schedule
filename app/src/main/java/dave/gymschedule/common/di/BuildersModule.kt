package dave.gymschedule.common.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dave.gymschedule.settings.view.SettingsActivity
import dave.gymschedule.schedule.view.GymScheduleActivity
import dave.gymschedule.schedule.view.GymScheduleFragment
import dave.gymschedule.SplashScreenActivity
import dave.gymschedule.widget.GymScheduleWidget

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun bindGymScheduleActivity(): GymScheduleActivity

    @ContributesAndroidInjector
    abstract fun bindSettingsActivity(): SettingsActivity

    @ContributesAndroidInjector
    abstract fun bindGymScheduleFragment() : GymScheduleFragment

    @ContributesAndroidInjector
    abstract fun bindSplashScreenActivity(): SplashScreenActivity

    @ContributesAndroidInjector
    abstract fun bindGymScheduleWidget(): GymScheduleWidget
}