package dave.gymschedule.presenter

import android.util.Log
import dave.gymschedule.GymScheduleApplication
import dave.gymschedule.interactor.EventTypeStateInteractor
import dave.gymschedule.interactor.GymScheduleInteractor
import dave.gymschedule.model.GymEvent
import dave.gymschedule.view.GymScheduleView
import io.reactivex.Observable
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

    private lateinit var date: Calendar

    init {
        GymScheduleApplication.graph.inject(this)
    }

    override fun onViewCreated(date: Calendar) {
        Log.d(TAG, "[${date.time}] getting schedule for date: ${date.time}")
        this.date = date
        eventTypeStateInteractor.getEventTypeMapPublishSubject()
                .subscribe { _ ->
                    Log.d(TAG, "[${date.time}] onViewCreated, received event states, getting gym events for date")
                    getGymEventForDate()
                }
    }

    override fun onViewDestroyed() {
        view = null
    }

    private fun getGymEventForDate() {
        view?.hideErrorMessage()
        view?.showLoadingIndicator()
        view?.updateSchedule(ArrayList())
        setDateText(date)
        updateEventList(date)
    }

    private fun updateEventList(date: Calendar) {
        scheduleInteractor.getGymEventsSingle(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapObservable { receivedGymEvents ->
                    gymEvents = receivedGymEvents
                    getVisibleEvents(gymEvents)
                }
                .subscribe({ visibleEvents ->
                    view?.updateSchedule(visibleEvents)
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

    private fun getVisibleEvents(gymEvents: List<GymEvent>): Observable<List<GymEvent>> {
        if (!eventTypeStateInteractor.anyEventTypesChecked()) {
            Log.d(TAG, "[${date.time}] no events checked, returning unmodified gym event list")
            return Observable.just(gymEvents)
        }
        return eventTypeStateInteractor.getEventTypeMapPublishSubject()
                .map { eventTypeMap ->
                    Log.d(TAG, "[${date.time}] got event states, filtering gym event list")
                    gymEvents.filter { eventTypeMap[it.eventType.eventTypeId] ?: false }
                }
    }

}
