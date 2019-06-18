package dave.gymschedule.presenter

import dave.gymschedule.model.EventType
import dave.gymschedule.repository.EventTypeStateRepository
import io.reactivex.Completable
import io.reactivex.Observable

class SettingsPresenter(private val eventTypeStateRepository: EventTypeStateRepository) {

    fun onEventTypeToggled(eventType: EventType, isEnabled: Boolean): Completable {
        return eventTypeStateRepository.updateEventTypeState(eventType, isEnabled)
    }

    fun getEventTypesMapObservable(): Observable<Map<Int, Boolean>> {
        return eventTypeStateRepository.eventTypeStatesPublisher
    }

}