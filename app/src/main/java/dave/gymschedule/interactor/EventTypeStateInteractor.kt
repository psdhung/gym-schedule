package dave.gymschedule.interactor

import android.util.Log
import dave.gymschedule.database.AppDatabase
import dave.gymschedule.database.EventTypeState
import dave.gymschedule.model.EventType
import io.reactivex.Completable
import io.reactivex.subjects.BehaviorSubject

class EventTypeStateInteractor(private val database: AppDatabase) {

    companion object {
        private val TAG = EventTypeStateInteractor::class.java.simpleName
    }

    private val eventTypeStates: MutableMap<Int, Boolean> = HashMap()
    private val eventTypeStatesPublisher: BehaviorSubject<MutableMap<Int, Boolean>> = BehaviorSubject.create()

    fun initialize(): Completable {
        return database.eventTypeStateDao().getAllEventTypeStates()
                .map { eventStates ->
                    Log.d(TAG, "got list, size=${eventStates.size}")
                    eventStates.forEach {
                        Log.d(TAG, "type=${it.eventTypeId}, checked=${it.checked}")
                        eventTypeStates[it.eventTypeId] = it.checked
                    }

                    Log.d(TAG, "constructor, publishing event states")
                    eventTypeStatesPublisher.onNext(eventTypeStates)
                }
                .toCompletable()
    }

    fun getEventTypeMapPublishSubject(): BehaviorSubject<MutableMap<Int, Boolean>> {
        return eventTypeStatesPublisher
    }

    fun updateEventTypeState(eventType: EventType, checked: Boolean): Completable {
        Log.d(TAG, "updating list, id=${eventType.eventTypeId}, checked=$checked")

        return database.eventTypeStateDao().updateEventTypeState(EventTypeState(eventType.eventTypeId, checked))
                .doOnComplete {
                    Log.d(TAG, "finished updating database")
                    eventTypeStates[eventType.eventTypeId] = checked
                    Log.d(TAG, "updateEventTypeState(), publishing event states")
                    eventTypeStatesPublisher.onNext(eventTypeStates)
                }
    }

    fun anyEventTypesChecked(): Boolean {
        return eventTypeStates.values.any { it }
    }

}