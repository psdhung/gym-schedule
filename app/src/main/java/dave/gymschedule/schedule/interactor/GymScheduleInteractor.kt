package dave.gymschedule.schedule.interactor

import dave.gymschedule.common.model.Resource
import dave.gymschedule.schedule.model.GymViewModel
import dave.gymschedule.schedule.repository.GymScheduleRepository
import dave.gymschedule.settings.model.GymLocation
import dave.gymschedule.settings.repository.EventFilterRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.Calendar

class GymScheduleInteractor(private val gymScheduleRepository: GymScheduleRepository,
                            private val eventFilterRepository: EventFilterRepository) {

    fun getGymEventViewModelsObservable(gymLocation: GymLocation, date: Calendar): Observable<Resource<GymViewModel>> {
        return Observable.combineLatest(
                gymScheduleRepository.getGymEventsViewModelObservable(gymLocation.locationId, date),
                eventFilterRepository.eventFilterObservable,
                BiFunction { resource, eventTypeMap ->
                    if (resource.status == Resource.Status.LOADING) {
                        Resource(Resource.Status.LOADING, GymViewModel(gymLocation, emptyList()))
                    } else if (resource.status == Resource.Status.ERROR) {
                        Resource(Resource.Status.ERROR, GymViewModel(gymLocation, emptyList()), resource.error)
                    } else {
                        val gymEventViewModels = resource.data
                        if (eventTypeMap.isEmpty() || eventTypeMap.all { !it.value }) {
                            Resource(Resource.Status.SUCCESS, GymViewModel(gymLocation, gymEventViewModels))
                        } else {
                            Resource(Resource.Status.SUCCESS, GymViewModel(gymLocation, gymEventViewModels.filter {
                                eventTypeMap[it.eventType.eventTypeId] ?: false
                            }))
                        }
                    }
                }
        )
    }
}
