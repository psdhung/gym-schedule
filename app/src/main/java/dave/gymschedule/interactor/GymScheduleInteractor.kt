package dave.gymschedule.interactor

import dave.gymschedule.Model.GymEvent
import io.reactivex.Single
import java.util.*

interface GymScheduleInteractor {
    fun getGymEventsObservable(date: Calendar): Single<List<GymEvent>>
}
