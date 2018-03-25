package dave.gymschedule.interactor

import android.util.Log
import dave.gymschedule.database.AppDatabase
import dave.gymschedule.model.EventType
import dave.gymschedule.database.EventTypeState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EventTypeStateInteractorImpl(private val database: AppDatabase) : EventTypeStateInteractor {

    companion object {
        private val TAG = EventTypeStateInteractorImpl::class.java.simpleName
    }

    private var eventTypeStates: MutableMap<Int, Boolean> = HashMap()

    init {
        Single.create<List<EventTypeState>> { emitter ->
            emitter.onSuccess(database.eventTypeStateDao().getAllEventTypeStates())
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    Log.d(TAG, "got list, size=${list.size}")
                    list.forEach {
                        Log.d(TAG, "type=${it.eventTypeId}, checked=${it.checked}")
                        eventTypeStates[it.eventTypeId] = it.checked
                    }
                }
    }

    override fun getStateOfEventType(eventType: EventType): Boolean {
        return eventTypeStates.getOrPut(eventType.eventTypeId, { false })
    }

    override fun updateEventTypeState(eventType: EventType, checked: Boolean) {
        Log.d(TAG, "got updating list, id=${eventType.eventTypeId}, checked=$checked")
        eventTypeStates[eventType.eventTypeId] = checked
        Single.create<Unit> { emitter ->
            database.eventTypeStateDao().updateEventTypeState(EventTypeState(eventType.eventTypeId, checked))
            emitter.onSuccess(Unit)
        }
                .subscribeOn(Schedulers.io())
                .subscribe { _ -> Log.d(TAG, "finished updating database") }
    }

    override fun anyEventTypesChecked(): Boolean {
        return eventTypeStates.values.any { it }
    }

}