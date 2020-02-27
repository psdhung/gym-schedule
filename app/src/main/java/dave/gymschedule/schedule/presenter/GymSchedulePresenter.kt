package dave.gymschedule.schedule.presenter

import dave.gymschedule.common.model.Resource
import dave.gymschedule.schedule.interactor.GymScheduleInteractor
import dave.gymschedule.schedule.model.GymViewModel
import dave.gymschedule.settings.GymLocationInteractor
import io.reactivex.Observable
import java.util.Calendar

class GymSchedulePresenter(private val gymScheduleInteractor: GymScheduleInteractor,
                           private val gymLocationInteractor: GymLocationInteractor) {

    fun getGymEventsForDate(date: Calendar): Observable<Resource<GymViewModel>> {
        return gymLocationInteractor.savedGymLocationObservable
                .flatMap { gymLocation ->
                    gymScheduleInteractor.getGymEventViewModelsObservable(gymLocation, date)
                }
    }

}