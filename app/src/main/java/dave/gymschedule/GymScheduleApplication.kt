package dave.gymschedule

import android.app.Application
import dave.gymschedule.di.DaggerGymScheduleComponent
import dave.gymschedule.di.GymScheduleComponent
import dave.gymschedule.di.AppModule

class GymScheduleApplication : Application() {

    companion object {
        @JvmStatic
        lateinit var graph: GymScheduleComponent
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerGymScheduleComponent.builder()
                .appModule(AppModule(this))
                .build()
        graph.inject(this)
    }
}
