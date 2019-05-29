package dave.gymschedule.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dave.gymschedule.view.GymScheduleActivity
import dave.gymschedule.view.GymScheduleFragment
import dave.gymschedule.view.SplashScreenActivity

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun bindSplashScreenActivity() : SplashScreenActivity

    @ContributesAndroidInjector
    abstract fun bindGymScheduleActivity(): GymScheduleActivity

    @ContributesAndroidInjector
    abstract fun bindGymScheduleFragment() : GymScheduleFragment

}