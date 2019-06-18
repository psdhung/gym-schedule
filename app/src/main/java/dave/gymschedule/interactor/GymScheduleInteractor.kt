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
        val gymEventViewModelObservable = gymScheduleRepository.getGymEventsViewModelSingle(date).toObservable()

        return Observable.combineLatest(
                gymEventViewModelObservable,
                eventTypeStateRepository.eventTypeStatesPublisher,
                BiFunction { gymEventViewModels: List<GymEventViewModel>, eventTypeMap: Map<Int, Boolean> ->
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