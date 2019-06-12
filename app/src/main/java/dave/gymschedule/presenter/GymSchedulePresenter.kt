package dave.gymschedule.presenter

import dave.gymschedule.interactor.GymScheduleInteractor
import dave.gymschedule.model.GymEventViewModel
import io.reactivex.Observable
import java.util.Calendar

class GymSchedulePresenter(private val gymScheduleInteractor: GymScheduleInteractor) {

    fun getGymEventsForDate(date: Calendar): Observable<List<GymEventViewModel>> {
        return gymScheduleInteractor.getGymEventViewModelsObservable(date)
    }

}