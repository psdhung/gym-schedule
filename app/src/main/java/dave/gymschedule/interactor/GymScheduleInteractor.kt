package dave.gymschedule.interactor

import dave.gymschedule.model.GymEvent
import io.reactivex.Single
import java.util.*

interface GymScheduleInteractor {
    fun getGymEventsSingle(date: Calendar): Single<List<GymEvent>>
}
