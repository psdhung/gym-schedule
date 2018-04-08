package dave.gymschedule.di

import android.arch.persistence.room.Room
import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import dagger.Module
import dagger.Provides
import dave.gymschedule.GymScheduleApplication
import dave.gymschedule.database.AppDatabase
import dave.gymschedule.interactor.EventTypeStateInteractor
import dave.gymschedule.interactor.EventTypeStateInteractorImpl
import dave.gymschedule.interactor.GymScheduleInteractor
import dave.gymschedule.interactor.GymScheduleInteractorImpl
import dave.gymschedule.transformer.GymEventTransformer
import javax.inject.Singleton

@Module
class AppModule(private val application: GymScheduleApplication) {

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
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "event-type-state-database").build()
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

    @Provides
    @Singleton
    fun providesEventTypeStateInteractor(appDatabase: AppDatabase): EventTypeStateInteractor {
        return EventTypeStateInteractorImpl(appDatabase)
    }

}