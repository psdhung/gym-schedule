package dave.gymschedule.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dave.gymschedule.GymScheduleApplicationImpl
import dave.gymschedule.transformer.GymEventTransformer
import javax.inject.Singleton

@Module
class GymScheduleModule(private val application: GymScheduleApplicationImpl) {

    @Provides
    @Singleton
    @ForApplication
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providesGymEventTransformer(): GymEventTransformer {
        return GymEventTransformer()
    }

//    @Provides
//    @Singleton
//    fun providesGymScheduleInteractor(application: GymScheduleApplication, transformer: GymEventTransformer): GymScheduleInteractor {
//        return GymScheduleInteractorImpl(application.requestQueue, transformer)
//    }
}