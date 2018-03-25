package dave.gymschedule.view

import dave.gymschedule.model.GymEvent

interface GymScheduleView {
    fun showLoadingIndicator()

    fun hideLoadingIndicator()

    fun disableAllRefreshButtons()

    fun enablePrevButton()

    fun disablePrevButton()

    fun enableTodayButton()

    fun enableNextButton()

    fun disableTodayButton()

    fun updateSchedule(gymEvents: List<GymEvent>)

    fun setDate(date: String)

    fun disableNextButton()

    fun showErrorMessage(errorMessage: String, error: Throwable)

    fun hideErrorMessage()
}
