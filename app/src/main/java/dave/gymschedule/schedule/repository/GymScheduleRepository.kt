package dave.gymschedule.schedule.repository

import android.util.Log
import dave.gymschedule.settings.model.EventType
import dave.gymschedule.schedule.model.GymEventViewModel
import dave.gymschedule.common.model.Resource
import dave.gymschedule.schedule.model.GymSchedule
import dave.gymschedule.schedule.service.YmcaService
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
        return Observable.create { emitter ->
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
                    val gymEventViewModels = convertGymScheduleToGymEventViewModels(gym)
                    gymEventViewModelsCache["$centreId-$formattedDateString"] = gymEventViewModels
                    emitter.onNext(Resource(Resource.Status.SUCCESS, gymEventViewModels))
                } catch (exception: Exception) {
                    emitter.onNext(Resource(Resource.Status.ERROR, listOf(), exception))
                }
            }
        }
    }

    private fun convertGymScheduleToGymEventViewModels(gymSchedule: GymSchedule?): List<GymEventViewModel> {
        val gymEventViewModels = mutableListOf<GymEventViewModel>()

        gymSchedule?.events?.forEach { gymEvents ->
            gymEvents.events.forEach { gymEvent ->
                gymEventViewModels.add(GymEventViewModel(
                        name = gymEvent.name,
                        eventType = EventType.getEventTypeFromId(gymEvent.eventType),
                        details = gymEvent.details,
                        startTime = gymEvent.startTime,
                        endTime = gymEvent.endTime,
                        location = gymEvent.location,
                        description = gymEvent.description,
                        fee = gymEvent.fee,
                        childMinding = gymEvent.childMinding,
                        ageRange = gymEvent.ageRange,
                        registration = gymEvent.registrationType
                ))
            }
        }

        return gymEventViewModels
    }

    private fun getFormattedDateString(calendar: Calendar): String {
        return String.format(Locale.getDefault(), "%d-%d-%d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH))
    }

}