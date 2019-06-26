package dave.gymschedule.repository

import android.util.Log
import dave.gymschedule.model.EventType
import dave.gymschedule.model.GymEventViewModel
import dave.gymschedule.service.YmcaService
import io.reactivex.Single
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

    fun getGymEventsViewModelSingle(date: Calendar): Single<List<GymEventViewModel>> {
        val formattedDateString = getFormattedDateString(date)

        val cachedGymEventViewModels = gymEventViewModelsCache[formattedDateString]
        if (!cachedGymEventViewModels.isNullOrEmpty()) {
            Log.d(TAG, "cache hit for date ${date.time}")
            return Single.just(cachedGymEventViewModels)
        }

        val startDateTime = "$formattedDateString+12:00:00+AM"
        val endDateTime = "$formattedDateString+11:59:59+PM"
        Log.d(TAG, "getting gym events for date ${date.time}")
        val gymScheduleSingle = ymcaService.getGymSchedule(startDateTime, endDateTime)
        return gymScheduleSingle.map { gym ->
            val gymEventViewModels = mutableListOf<GymEventViewModel>()

            gym.events.forEach { gymEvents ->
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


            gymEventViewModelsCache[formattedDateString] = gymEventViewModels
            gymEventViewModels
        }
    }

    private fun getFormattedDateString(calendar: Calendar): String {
        return String.format(Locale.getDefault(), "%d-%d-%d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH))
    }

}