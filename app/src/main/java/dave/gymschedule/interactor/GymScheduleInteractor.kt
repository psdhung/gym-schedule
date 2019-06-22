package dave.gymschedule.interactor

import dave.gymschedule.model.GymEventViewModel
import dave.gymschedule.repository.EventTypeStateRepository
import dave.gymschedule.repository.GymScheduleRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.Calendar

class GymScheduleInteractor(private val gymScheduleRepository: GymScheduleRepository,
                            private val eventTypeStateRepository: EventTypeStateRepository) {

    fun getGymEventViewModelsObservable(date: Calendar): Observable<List<GymEventViewModel>> {
        return Observable.combineLatest(
                gymScheduleRepository.getGymEventsViewModelSingle(date).toObservable(),
                eventTypeStateRepository.eventTypeStateObservable,
                BiFunction { gymEventViewModels, eventTypeMap ->
                    if (eventTypeMap.isEmpty() || eventTypeMap.all { !it.value }) {
                        gymEventViewModels
                    } else {
                        gymEventViewModels.filter {
                            eventTypeMap[it.eventType.eventTypeId] ?: false
                        }
                    }
                }
        )
    }

}