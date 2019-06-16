package dave.gymschedule.repository

import android.util.Log
import dave.gymschedule.database.AppDatabase
import dave.gymschedule.database.EventTypeState
import dave.gymschedule.model.EventType
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import io.reactivex.subjects.BehaviorSubject

class EventTypeStateRepository(private val database: AppDatabase) {

    companion object {
        private val TAG = EventTypeStateRepository::class.java.simpleName
    }

    val eventTypeStatesPublisher: BehaviorSubject<Map<Int, Boolean>> = BehaviorSubject.create()

    init {
        val eventTypeStatesLiveData = database.eventTypeStateDao()
                .getAllEventTypeStates()

        eventTypeStatesLiveData.observeForever { eventTypeStates ->
            Log.d(TAG, "event types updated: $eventTypeStates")
            val eventTypeMap = mutableMapOf<Int, Boolean>()
            eventTypeStates.forEach { eventTypeState ->
                Log.d(TAG, "${eventTypeState.eventTypeId} = ${eventTypeState.enabled}")
                eventTypeMap[eventTypeState.eventTypeId] = eventTypeState.enabled
            }
            eventTypeStatesPublisher.onNext(eventTypeMap)
        }
    }

    fun updateEventTypeState(eventType: EventType, checked: Boolean): Completable {
        return database.eventTypeStateDao()
                .updateEventTypeState(EventTypeState(eventType.eventTypeId, checked))
                .subscribeOn(io())
                .observeOn(mainThread())
    }

}