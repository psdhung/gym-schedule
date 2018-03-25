package dave.gymschedule.presenter

import dave.gymschedule.Model.EventType

interface GymSchedulePresenter {
    fun onPrevPressed()

    fun onTodayPressed()

    fun onNextPressed()

    fun onViewCreated()

    fun onEventCategoryToggled(checked: Boolean, gymEvents: EventType)

    fun isEventCategoryChecked(gymEvents: EventType): Boolean
}