package dave.gymschedule.presenter

import dave.gymschedule.interactor.EventTypeStateInteractor
import dave.gymschedule.interactor.GymScheduleInteractor
import dave.gymschedule.model.GymEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Calendar

interface GymSchedulePresenter {
    fun getGymEventsForDate(date: Calendar): Observable<List<GymEvent>>
}

class GymSchedulePresenterImpl(private val scheduleInteractor: GymScheduleInteractor,
                               private val eventTypeStateInteractor: EventTypeStateInteractor) : GymSchedulePresenter {

    override fun getGymEventsForDate(date: Calendar): Observable<List<GymEvent>> {
        return scheduleInteractor.getGymEventsSingle(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapObservable { receivedGymEvents ->
                    getVisibleEvents(receivedGymEvents)
                }
    }

    private fun getVisibleEvents(gymEvents: List<GymEvent>): Observable<List<GymEvent>> {
        return if (eventTypeStateInteractor.anyEventTypesChecked()) {
            eventTypeStateInteractor.getEventTypeMapPublishSubject()
                    .map { eventTypeMap ->
                        gymEvents.filter { eventTypeMap[it.eventType.eventTypeId] ?: false }
                    }
        } else {
            Observable.just(gymEvents)
        }
    }
}