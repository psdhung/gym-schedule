package dave.gymschedule.interactor

import dave.gymschedule.model.EventType
import io.reactivex.subjects.BehaviorSubject

interface EventTypeStateInteractor {
    fun getEventTypeMapPublishSubject(): BehaviorSubject<MutableMap<Int, Boolean>>
    fun updateEventTypeState(eventType: EventType, checked: Boolean)
    fun anyEventTypesChecked(): Boolean
}