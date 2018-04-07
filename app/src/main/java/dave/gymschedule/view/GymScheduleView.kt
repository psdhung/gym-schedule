package dave.gymschedule.view

import dave.gymschedule.model.GymEvent

interface GymScheduleView {
    fun showLoadingIndicator()

    fun hideLoadingIndicator()

    fun updateSchedule(gymEvents: List<GymEvent>)

    fun setDate(date: String)

    fun showErrorMessage(errorMessage: String, error: Throwable)

    fun hideErrorMessage()
}
