package dave.gymschedule.presenter

import android.util.Log
import dave.gymschedule.GymScheduleApplication
import dave.gymschedule.interactor.EventTypeStateInteractor
import dave.gymschedule.interactor.GymScheduleInteractor
import dave.gymschedule.model.EventType
import dave.gymschedule.model.GymEvent
import dave.gymschedule.view.GymScheduleView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GymSchedulePresenterImpl(private var view: GymScheduleView?) : GymSchedulePresenter {

    companion object {
        private val TAG = GymSchedulePresenterImpl::class.java.simpleName

        private val DISPLAYED_DATE_FORMAT = SimpleDateFormat("MMM dd, YYYY", Locale.getDefault())
    }

    @Inject
    lateinit var scheduleInteractor: GymScheduleInteractor

    @Inject
    lateinit var eventTypeStateInteractor: EventTypeStateInteractor

    private var gymEvents: List<GymEvent> = emptyList()

    init {
        GymScheduleApplication.graph.inject(this)
    }

    override fun onViewCreated(date: Calendar) {
        Log.d(TAG, "getting schedule for date: ${date.time}")
        getGymEventForDate(date)
    }

    override fun onViewDestroyed() {
        view = null
    }

    private fun getGymEventForDate(date: Calendar) {
        view?.hideErrorMessage()
        view?.showLoadingIndicator()
        view?.updateSchedule(ArrayList())
        setDateText(date)
        scheduleInteractor.getGymEventsObservable(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ receivedGymEvents ->
                    gymEvents = receivedGymEvents
                    view?.updateSchedule(getVisibleEvents(gymEvents))
                    view?.hideLoadingIndicator()
                }, { error ->
                    Log.d(TAG, "failed to retrieve schedule", error)
                    view?.hideLoadingIndicator()
                    view?.showErrorMessage("Could not retrieve schedule", error)
                })
    }

    private fun setDateText(date: Calendar) {
        view?.setDate(DISPLAYED_DATE_FORMAT.format(date.time))
    }

    override fun isEventCategoryChecked(eventType: EventType): Boolean {
        return eventTypeStateInteractor.getStateOfEventType(eventType)
    }

    override fun onEventCategoryToggled(eventType: EventType, checked: Boolean) {
        eventTypeStateInteractor.updateEventTypeState(eventType, checked)
        view?.updateSchedule(getVisibleEvents(gymEvents))
    }

    private fun getVisibleEvents(gymEvents: List<GymEvent>): List<GymEvent> {
        if (!eventTypeStateInteractor.anyEventTypesChecked()) {
            return gymEvents
        }
        return gymEvents.filter { it -> eventTypeStateInteractor.getStateOfEventType(it.eventType) }
    }

}
