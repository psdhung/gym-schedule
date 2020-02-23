package dave.gymschedule.settings.repository

import android.util.Log
import dave.gymschedule.common.database.AppDatabase
import dave.gymschedule.common.database.EventTypeState
import dave.gymschedule.common.model.EventType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io

class EventTypeStateRepository(private val database: AppDatabase) {

    companion object {
        private val TAG = EventTypeStateRepository::class.java.simpleName
    }

    val eventTypeStateObservable: Observable<Map<Int, Boolean>> = database.eventTypeStateDao()
            .getAllEventTypeStates()
            .map { eventTypeStates ->
                Log.d(TAG, "event types updated: $eventTypeStates")
                val eventTypeMap = mutableMapOf<Int, Boolean>()
                eventTypeStates.forEach { eventTypeState ->
                    Log.d(TAG, "${eventTypeState.eventTypeId} = ${eventTypeState.enabled}")
                    eventTypeMap[eventTypeState.eventTypeId] = eventTypeState.enabled
                }

                eventTypeMap
            }

    fun updateEventTypeState(eventType: EventType, checked: Boolean): Completable {
        return database.eventTypeStateDao()
                .updateEventTypeState(EventTypeState(eventType.eventTypeId, checked))
                .subscribeOn(io())
                .observeOn(mainThread())
    }

}