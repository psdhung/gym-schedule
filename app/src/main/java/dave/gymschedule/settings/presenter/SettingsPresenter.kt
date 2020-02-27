package dave.gymschedule.settings.presenter

import dave.gymschedule.settings.model.EventType
import dave.gymschedule.settings.repository.EventFilterRepository
import io.reactivex.Completable
import io.reactivex.Observable

class SettingsPresenter(private val eventFilterRepository: EventFilterRepository) {

    fun onEventTypeToggled(eventType: EventType, isEnabled: Boolean): Completable {
        return eventFilterRepository.updateEventFilter(eventType, isEnabled)
    }

    fun getEventTypesMapObservable(): Observable<Map<Int, Boolean>> {
        return eventFilterRepository.eventFilterObservable
    }

}