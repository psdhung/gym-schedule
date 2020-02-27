package dave.gymschedule.settings.repository

import android.util.Log
import dave.gymschedule.common.database.AppDatabase
import dave.gymschedule.common.database.EventFilter
import dave.gymschedule.settings.model.EventType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io

class EventFilterRepository(private val database: AppDatabase) {

    companion object {
        private val TAG = EventFilterRepository::class.java.simpleName
    }

    val eventFilterObservable: Observable<Map<Int, Boolean>> = database.eventTypeFilterDao()
            .getAllEventFilters()
            .map { eventFilters ->
                Log.d(TAG, "event filters updated: $eventFilters")

                val eventTypeMap = mutableMapOf<Int, Boolean>()
                eventFilters.forEach { eventFilter ->
                    Log.d(TAG, "${eventFilter.eventTypeId} = ${eventFilter.enabled}")
                    eventTypeMap[eventFilter.eventTypeId] = eventFilter.enabled
                }
                eventTypeMap
            }

    fun updateEventFilter(eventType: EventType, enabled: Boolean): Completable {
        return database.eventTypeFilterDao()
                .updateEventFilter(EventFilter(eventType.eventTypeId, enabled))
                .subscribeOn(io())
                .observeOn(mainThread())
    }

}