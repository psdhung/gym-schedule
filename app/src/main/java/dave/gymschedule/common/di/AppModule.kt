package dave.gymschedule.common.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dave.gymschedule.R
import dave.gymschedule.common.database.AppDatabase
import dave.gymschedule.common.database.GymLocationRepository
import dave.gymschedule.schedule.interactor.GymScheduleInteractor
import dave.gymschedule.schedule.presenter.GymSchedulePresenter
import dave.gymschedule.settings.presenter.SettingsPresenter
import dave.gymschedule.settings.repository.EventFilterRepository
import dave.gymschedule.schedule.repository.GymScheduleRepository
import dave.gymschedule.schedule.service.YmcaService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
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
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
            eventFilterRepository: EventFilterRepository,
            gymLocationRepository: GymLocationRepository): GymScheduleInteractor {
        return GymScheduleInteractor(gymScheduleRepository, eventFilterRepository, gymLocationRepository)
    }

    @Provides
    @Singleton
    fun providesEventFilterRepository(appDatabase: AppDatabase): EventFilterRepository {
        return EventFilterRepository(appDatabase)
    }

    @Provides
    @Singleton
    fun providesGymScheduleRepository(ymcaService: YmcaService): GymScheduleRepository {
        return GymScheduleRepository(ymcaService)
    }

    @Provides
    @Singleton
    fun providesSettingsPresenter(eventFilterRepository: EventFilterRepository): SettingsPresenter {
        return SettingsPresenter(eventFilterRepository)
    }

    @Provides
    @Singleton
    fun providesGymLocationRepository(@ApplicationContext context: Context): GymLocationRepository {
        return GymLocationRepository(context.getSharedPreferences(context.getString(R.string.app_shared_preferences_file_key), Context.MODE_PRIVATE))
    }
}