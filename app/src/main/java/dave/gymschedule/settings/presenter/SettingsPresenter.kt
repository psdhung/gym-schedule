package dave.gymschedule.settings.presenter

import dave.gymschedule.common.model.EventType
import dave.gymschedule.settings.repository.EventTypeStateRepository
import io.reactivex.Completable
import io.reactivex.Observable

class SettingsPresenter(private val eventTypeStateRepository: EventTypeStateRepository) {

    fun onEventTypeToggled(eventType: EventType, isEnabled: Boolean): Completable {
        return eventTypeStateRepository.updateEventTypeState(eventType, isEnabled)
    }

    fun getEventTypesMapObservable(): Observable<Map<Int, Boolean>> {
        return eventTypeStateRepository.eventTypeStateObservable
    }

}