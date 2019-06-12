package dave.gymschedule.interactor

import dave.gymschedule.model.GymEventViewModel
import dave.gymschedule.repository.EventTypeStateRepository
import dave.gymschedule.repository.GymScheduleRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.Calendar

class GymScheduleInteractor(private val gymScheduleRepository: GymScheduleRepository,
                            private val eventTypeStateRepository: EventTypeStateRepository) {

    companion object {
        private val TAG = GymScheduleInteractor::class.java.simpleName
    }

    fun getGymEventViewModelsObservable(date: Calendar): Observable<List<GymEventViewModel>> {
        val gymEventViewModelObservable = gymScheduleRepository.getGymEventsViewModelSingle(date).toObservable()
        return if (eventTypeStateRepository.areAnyEventTypesChecked()) {
            Observable.combineLatest(
                    gymEventViewModelObservable,
                    eventTypeStateRepository.getEventTypeMapPublishSubject(),
                    BiFunction { gymEventViewModels: List<GymEventViewModel>, eventTypeMap: Map<Int, Boolean> ->
                        gymEventViewModels.filter {
                            eventTypeMap[it.eventType.eventTypeId] ?: false
                        }
                    })
        } else {
            gymEventViewModelObservable
        }
    }
}