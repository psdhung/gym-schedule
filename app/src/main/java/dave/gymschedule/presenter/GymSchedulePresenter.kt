package dave.gymschedule.presenter

import dave.gymschedule.Model.EventType

interface GymSchedulePresenter {
    fun onPrevPressed()

    fun onTodayPressed()

    fun onNextPressed()

    fun onViewCreated()

    fun onEventCategoryToggled(checked: Boolean, pooL_ACTIVITIES: EventType)

    fun isEventCategoryChecked(pooL_ACTIVITIES: EventType): Boolean
}