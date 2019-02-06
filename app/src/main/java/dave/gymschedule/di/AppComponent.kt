package dave.gymschedule.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import dave.gymschedule.GymScheduleApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    BuildersModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: GymScheduleApplication): Builder

        fun build(): AppComponent
    }

    fun inject(application: GymScheduleApplication)
}