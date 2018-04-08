package dave.gymschedule.interactor

import android.util.Log
import dave.gymschedule.database.AppDatabase
import dave.gymschedule.database.EventTypeState
import dave.gymschedule.model.EventType
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class EventTypeStateInteractorImpl(private val database: AppDatabase) : EventTypeStateInteractor {

    companion object {
        private val TAG = EventTypeStateInteractorImpl::class.java.simpleName
    }

    private val eventTypeStates: MutableMap<Int, Boolean> = HashMap()
    private val eventTypeStatesPublisher: BehaviorSubject<MutableMap<Int, Boolean>> = BehaviorSubject.create()

    init {
        Single.create<List<EventTypeState>> { emitter ->
            emitter.onSuccess(database.eventTypeStateDao().getAllEventTypeStates())
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { eventStates ->
                    Log.d(TAG, "got list, size=${eventStates.size}")
                    eventStates.forEach {
                        Log.d(TAG, "type=${it.eventTypeId}, checked=${it.checked}")
                        eventTypeStates[it.eventTypeId] = it.checked
                    }

                    Log.d(TAG, "constructor, publishing event states")
                    eventTypeStatesPublisher.onNext(eventTypeStates)
                }
    }

    override fun getEventTypeMapPublishSubject(): BehaviorSubject<MutableMap<Int, Boolean>> {
        return eventTypeStatesPublisher
    }

    override fun updateEventTypeState(eventType: EventType, checked: Boolean) {
        Log.d(TAG, "got updating list, id=${eventType.eventTypeId}, checked=$checked")
        Single.create<Unit> { emitter ->
            database.eventTypeStateDao().updateEventTypeState(EventTypeState(eventType.eventTypeId, checked))
            emitter.onSuccess(Unit)
        }
                .subscribeOn(Schedulers.io())
                .subscribe { _ -> Log.d(TAG, "finished updating database") }

        eventTypeStates[eventType.eventTypeId] = checked
        Log.d(TAG, "updateEventTypeState(), publishing event states")
        eventTypeStatesPublisher.onNext(eventTypeStates)
    }

    override fun anyEventTypesChecked(): Boolean {
        return eventTypeStates.values.any { it }
    }

}