package dave.gymschedule.interactor


import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import dave.gymschedule.Model.GymEvent
import dave.gymschedule.transformer.GymEventTransformer
import io.reactivex.Single
import java.util.*

class GymScheduleInteractorImpl(private val requestQueue: RequestQueue, private val transformer: GymEventTransformer) : GymScheduleInteractor {
    companion object {
        private const val SCHEDULE_REQUEST_URL = "https://api.ymcagta.org/api/Classes/GetByCentreId?centreId=39&startDateTime=%1\$s+12:00:00+AM&endDateTime=%1\$s+11:59:59+PM"
    }

    override fun getGymEventsObservable(date: Calendar): Single<List<GymEvent>> {
        val requestString = String.format(SCHEDULE_REQUEST_URL, getFormattedDateString(date))
        return Single.create { emitter ->
            requestQueue.add(
                    JsonObjectRequest(Request.Method.GET, requestString, null,
                            Response.Listener { response -> emitter.onSuccess(transformer.getGymEventsFromJson(response)) },
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