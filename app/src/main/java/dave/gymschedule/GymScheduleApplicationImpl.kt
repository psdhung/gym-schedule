package dave.gymschedule

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import dave.gymschedule.di.DaggerGymScheduleComponent
import dave.gymschedule.di.GymScheduleComponent
import dave.gymschedule.di.GymScheduleModule


class GymScheduleApplicationImpl : Application(), GymScheduleApplication {

    companion object {
        @JvmStatic
        lateinit var graph: GymScheduleComponent
    }

    private lateinit var _requestQueue: RequestQueue

    override fun onCreate() {
        super.onCreate()
        graph = DaggerGymScheduleComponent.builder().gymScheduleModule(GymScheduleModule(this)).build()
        graph.inject(this)
    }

    override val requestQueue: RequestQueue
        get() {
            if (!::_requestQueue.isInitialized) {
                _requestQueue = Volley.newRequestQueue(applicationContext)
            }

            return _requestQueue
        }
}
