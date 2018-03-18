package dave.gymschedule

import android.app.Application

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class PoolScheduleApplicationImpl : Application(), GymEventApplication {
    private lateinit var _requestQueue: RequestQueue
    override val requestQueue: RequestQueue
        get() {
            if (!::_requestQueue.isInitialized) {
                _requestQueue = Volley.newRequestQueue(applicationContext)
            }

            return _requestQueue
        }
}
