package dave.gymschedule.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dave.gymschedule.database.AppDatabase
import dave.gymschedule.interactor.GymScheduleInteractor
import dave.gymschedule.presenter.GymSchedulePresenter
import dave.gymschedule.presenter.SettingsPresenter
import dave.gymschedule.repository.EventTypeStateRepository
import dave.gymschedule.repository.GymScheduleRepository
import dave.gymschedule.service.YmcaService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    @ApplicationContext
    fun provideApplicationContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun providesRetrofit(@ApplicationContext context: Context): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(Cache(context.cacheDir, 10 * 1024 * 1024))
                .build()

        return Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.ymcagta.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun providesYmcaService(retrofit: Retrofit): YmcaService {
        return retrofit.create(YmcaService::class.java)
    }

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "event-type-state-database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesGymSchedulePresenter(scheduleInteractor: GymScheduleInteractor): GymSchedulePresenter {
        return GymSchedulePresenter(scheduleInteractor)
    }

    @Provides
    @Singleton
    fun providesGymScheduleInteractor(
            gymScheduleRepository: GymScheduleRepository,
            eventTypeStateRepository: EventTypeStateRepository): GymScheduleInteractor {
        return GymScheduleInteractor(gymScheduleRepository, eventTypeStateRepository)
    }

    @Provides
    @Singleton
    fun providesEventTypeStateInteractor(appDatabase: AppDatabase): EventTypeStateRepository {
        return EventTypeStateRepository(appDatabase)
    }

    @Provides
    @Singleton
    fun providesGymScheduleRepository(ymcaService: YmcaService): GymScheduleRepository {
        return GymScheduleRepository(ymcaService)
    }

    @Provides
    @Singleton
    fun providesSettingsPresenter(eventTypeStateRepository: EventTypeStateRepository): SettingsPresenter {
        return SettingsPresenter(eventTypeStateRepository)
    }
}