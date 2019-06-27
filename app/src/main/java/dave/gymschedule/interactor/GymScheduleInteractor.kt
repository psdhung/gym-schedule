package dave.gymschedule.interactor

import dave.gymschedule.database.GymLocationRepository
import dave.gymschedule.model.GymEventViewModel
import dave.gymschedule.repository.EventTypeStateRepository
import dave.gymschedule.repository.GymScheduleRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers.io
import java.util.Calendar

class GymScheduleInteractor(private val gymScheduleRepository: GymScheduleRepository,
                            private val eventTypeStateRepository: EventTypeStateRepository,
                            private val gymLocationRepository: GymLocationRepository) {

    fun getGymEventViewModelsObservable(date: Calendar): Observable<List<GymEventViewModel>> {
        return Observable.combineLatest(
                gymLocationRepository.savedGymLocationIdObservable
                        .observeOn(io())
                        .flatMap { savedGymLocationId ->
                            gymScheduleRepository.getGymEventsViewModelSingle(savedGymLocationId, date).toObservable()
                        },
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
