package dave.gymschedule.presenter

import java.util.Calendar

interface GymSchedulePresenter {

    fun onViewCreated(date: Calendar)

    fun onViewDestroyed()

}