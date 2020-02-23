package dave.gymschedule.schedule.presenter

import dave.gymschedule.schedule.interactor.GymScheduleInteractor
import dave.gymschedule.schedule.model.GymEventViewModel
import dave.gymschedule.common.model.Resource
import io.reactivex.Observable
import java.util.Calendar

class GymSchedulePresenter(private val gymScheduleInteractor: GymScheduleInteractor) {

    fun getGymEventsForDate(date: Calendar): Observable<Resource<List<GymEventViewModel>>> {
        return gymScheduleInteractor.getGymEventViewModelsObservable(date)
    }

}