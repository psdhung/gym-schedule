package dave.gymschedule.presenter

import dave.gymschedule.model.EventType

interface GymSchedulePresenter {
    fun onPrevPressed()

    fun onTodayPressed()

    fun onNextPressed()

    fun onViewCreated()

    fun onEventCategoryToggled(eventType: EventType, checked: Boolean)

    fun isEventCategoryChecked(eventType: EventType): Boolean
}