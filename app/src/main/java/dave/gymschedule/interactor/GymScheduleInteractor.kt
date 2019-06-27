package dave.gymschedule.interactor

import dave.gymschedule.database.GymLocationRepository
import dave.gymschedule.model.GymEventViewModel
import dave.gymschedule.model.Resource
import dave.gymschedule.repository.EventTypeStateRepository
import dave.gymschedule.repository.GymScheduleRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers.io
import java.util.Calendar

class GymScheduleInteractor(private val gymScheduleRepository: GymScheduleRepository,
                            private val eventTypeStateRepository: EventTypeStateRepository,
                            private val gymLocationRepository: GymLocationRepository) {

    fun getGymEventViewModelsObservable(date: Calendar): Observable<Resource<List<GymEventViewModel>>> {
        return Observable.combineLatest(
                gymLocationRepository.savedGymLocationIdObservable
                        .observeOn(io())
                        .flatMap { savedGymLocationId ->
                            gymScheduleRepository.getGymEventsViewModelObservable(savedGymLocationId, date)
                        },
                eventTypeStateRepository.eventTypeStateObservable,
                BiFunction { gymEventViewModelsResource, eventTypeMap ->
                    if (gymEventViewModelsResource.status == Resource.Status.LOADING) {
                        Resource(Resource.Status.LOADING, listOf())
                    } else if (gymEventViewModelsResource.status == Resource.Status.ERROR) {
                        Resource(Resource.Status.ERROR, listOf(), gymEventViewModelsResource.error)
                    } else {
                        val gymEventViewModels = gymEventViewModelsResource.data
                        if (eventTypeMap.isEmpty() || eventTypeMap.all { !it.value }) {
                            Resource(Resource.Status.SUCCESS, gymEventViewModels)
                        } else {
                            Resource(Resource.Status.SUCCESS, gymEventViewModels.filter {
                                eventTypeMap[it.eventType.eventTypeId] ?: false
                            })
                        }
                    }
                }
        )
    }
}
