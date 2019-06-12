package dave.gymschedule.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import dagger.Module
import dagger.Provides
import dave.gymschedule.database.AppDatabase
import dave.gymschedule.interactor.EventTypeStateInteractor
import dave.gymschedule.interactor.GymScheduleInteractor
import dave.gymschedule.presenter.GymSchedulePresenter
import dave.gymschedule.transformer.GymEventTransformer
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    @ApplicationContext
    fun provideApplicationContext(application: Application): Context = application.applicationContext

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
    fun providesGymSchedulePresenter(scheduleInteractor: GymScheduleInteractor, eventTypeStateInteractor: EventTypeStateInteractor): GymSchedulePresenter {
        return GymSchedulePresenter(scheduleInteractor, eventTypeStateInteractor)
    }

    @Provides
    @Singleton
    fun providesGymScheduleInteractor(requestQueue: RequestQueue, transformer: GymEventTransformer): GymScheduleInteractor {
        return GymScheduleInteractor(requestQueue, transformer)
    }

    @Provides
    @Singleton
    fun providesEventTypeStateInteractor(appDatabase: AppDatabase): EventTypeStateInteractor {
        return EventTypeStateInteractor(appDatabase)
    }

}