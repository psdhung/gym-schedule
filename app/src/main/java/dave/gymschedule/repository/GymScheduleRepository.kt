package dave.gymschedule.repository

import android.util.Log
import dave.gymschedule.model.EventType
import dave.gymschedule.model.GymEventViewModel
import dave.gymschedule.model.Resource
import dave.gymschedule.service.YmcaService
import io.reactivex.Observable
import java.lang.Exception
import java.util.Calendar
import java.util.Locale

class GymScheduleRepository(private val ymcaService: YmcaService) {

    companion object {
        private val TAG = GymScheduleRepository::class.java.simpleName
    }

    /*
     The responses for the gym schedule always have max-age=0 so OkHttp won't cache the result.
     Use a memory cache for now to reduce the number of network calls.

     TODO Add a network interceptor to OkHttp to set max-age to a non-zero number and remove this memory cache.
      */
    private val gymEventViewModelsCache = mutableMapOf<String, List<GymEventViewModel>>()

    fun getGymEventsViewModelObservable(centreId: Int, date: Calendar): Observable<Resource<List<GymEventViewModel>>> {
        return Observable.create<Resource<List<GymEventViewModel>>> { emitter ->
            val formattedDateString = getFormattedDateString(date)

            val cachedGymEventViewModels = gymEventViewModelsCache["$centreId-$formattedDateString"]
            if (!cachedGymEventViewModels.isNullOrEmpty()) {
                Log.d(TAG, "cache hit for date ${date.time} at centreId $centreId")
                emitter.onNext(Resource(Resource.Status.SUCCESS, cachedGymEventViewModels))
            } else {
                emitter.onNext(Resource(Resource.Status.LOADING, listOf()))

                val startDateTime = "$formattedDateString+12:00:00+AM"
                val endDateTime = "$formattedDateString+11:59:59+PM"
                Log.d(TAG, "getting gym events for date ${date.time} and centreId $centreId")

                try {
                    val gym = ymcaService.getGymSchedule(centreId, startDateTime, endDateTime).execute().body()

//                gymScheduleSingle.map { gym ->
                    val gymEventViewModels = mutableListOf<GymEventViewModel>()

                    gym?.events?.forEach { gymEvents ->
                        gymEvents.events.forEach { gymEvent ->
                            gymEventViewModels.add(GymEventViewModel(
                                    gymEvent.name,
                                    EventType.getEventTypeFromId(gymEvent.eventType),
                                    gymEvent.details,
                                    gymEvent.startTime,
                                    gymEvent.endTime,
                                    gymEvent.location,
                                    gymEvent.description,
                                    gymEvent.fee,
                                    gymEvent.childMinding,
                                    gymEvent.ageRange,
                                    gymEvent.registrationType
                            ))
                        }
                    }

                    gymEventViewModelsCache["$centreId-$formattedDateString"] = gymEventViewModels
                    emitter.onNext(Resource(Resource.Status.SUCCESS, gymEventViewModels))
//                }
                } catch (exception: Exception) {
                    emitter.onNext(Resource(Resource.Status.ERROR, listOf(), exception))
                }
            }
        }
    }

    private fun getFormattedDateString(calendar: Calendar): String {
        return String.format(Locale.getDefault(), "%d-%d-%d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH))
    }

}