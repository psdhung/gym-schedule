package dave.gymschedule.interactor

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import dave.gymschedule.model.GymEvent
import dave.gymschedule.transformer.GymEventTransformer
import io.reactivex.Single
import java.util.Calendar
import java.util.HashMap
import java.util.Locale

class GymScheduleInteractor(private val requestQueue: RequestQueue,
                            private val transformer: GymEventTransformer) {
    companion object {
        private val TAG = GymScheduleInteractor::class.java.simpleName
        private const val SCHEDULE_REQUEST_URL = "https://api.ymcagta.org/api/Classes/GetByCentreId?centreId=39&startDateTime=%1\$s+12:00:00+AM&endDateTime=%1\$s+11:59:59+PM"
    }

    private val gymEventsMap: MutableMap<Long, List<GymEvent>> = HashMap()

    fun getGymEventsSingle(date: Calendar): Single<List<GymEvent>> {
        val key = date.timeInMillis
        if (gymEventsMap.contains(key)) {
            Log.d(TAG, "memory cache hit for date ${date.time}, returning cached events")
            return Single.just(gymEventsMap[key])
        }

        val requestString = String.format(SCHEDULE_REQUEST_URL, getFormattedDateString(date))
        return Single.create { emitter ->
            requestQueue.add(
                    JsonObjectRequest(Request.Method.GET, requestString, null,
                            Response.Listener { response ->
                                val gymEvents = transformer.getGymEventsFromJson(response)
                                gymEventsMap[key] = gymEvents
                                emitter.onSuccess(gymEvents)
                            },
                            Response.ErrorListener { error -> emitter.onError(error) }))
        }
    }

    private fun getFormattedDateString(calendar: Calendar): String {
        return String.format(Locale.getDefault(), "%d-%d-%d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH))
    }
}