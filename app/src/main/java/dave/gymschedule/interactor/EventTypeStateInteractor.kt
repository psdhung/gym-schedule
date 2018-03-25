package dave.gymschedule.interactor

import dave.gymschedule.model.EventType

interface EventTypeStateInteractor {
    fun getStateOfEventType(eventType: EventType): Boolean
    fun updateEventTypeState(eventType: EventType, checked: Boolean)
    fun anyEventTypesChecked(): Boolean
}