package dave.gymschedule.presenter

import dave.gymschedule.model.EventType
import java.util.Calendar

interface GymSchedulePresenter {

    fun onViewCreated(date: Calendar)

    fun onViewDestroyed()

    fun onEventCategoryToggled(eventType: EventType, checked: Boolean)

    fun isEventCategoryChecked(eventType: EventType): Boolean
}