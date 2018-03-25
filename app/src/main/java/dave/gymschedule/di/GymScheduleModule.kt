package dave.gymschedule.di

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import dagger.Module
import dagger.Provides
import dave.gymschedule.GymScheduleApplication
import dave.gymschedule.interactor.GymScheduleInteractor
import dave.gymschedule.interactor.GymScheduleInteractorImpl
import dave.gymschedule.transformer.GymEventTransformer
import javax.inject.Singleton

@Module
class GymScheduleModule(private val application: GymScheduleApplication) {

    @Provides
    @Singleton
    @ApplicationContext
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providesRequestQueue(@ApplicationContext context: Context): RequestQueue {
        return Volley.newRequestQueue(context)
    }

    @Provides
    @Singleton
    fun providesGymEventTransformer(): GymEventTransformer {
        return GymEventTransformer()
    }

    @Provides
    @Singleton
    fun providesGymScheduleInteractor(requestQueue: RequestQueue, transformer: GymEventTransformer): GymScheduleInteractor {
        return GymScheduleInteractorImpl(requestQueue, transformer)
    }

}