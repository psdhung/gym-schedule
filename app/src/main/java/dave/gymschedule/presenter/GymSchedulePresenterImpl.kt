package dave.gymschedule.presenter

import android.util.Log
import dave.gymschedule.GymScheduleApplication
import dave.gymschedule.Model.EventType
import dave.gymschedule.Model.GymEvent
import dave.gymschedule.interactor.GymScheduleInteractor
import dave.gymschedule.view.GymScheduleView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GymSchedulePresenterImpl (private val view: GymScheduleView) : GymSchedulePresenter {

    companion object {
        private val TAG = GymSchedulePresenterImpl::class.java.simpleName

        private val DISPLAYED_DATE_FORMAT = SimpleDateFormat("MMM dd, YYYY", Locale.getDefault())
    }

    @Inject
    lateinit var interactor: GymScheduleInteractor

    private val TODAY: Calendar
    private val MAX_FUTURE_DAY: Calendar
    private var currentShownDay: Calendar

    private var gymEvents: List<GymEvent> = emptyList()

    private val todayString: Calendar
        get() {
            currentShownDay = Calendar.getInstance()
            return currentShownDay
        }

    init {
        GymScheduleApplication.graph.inject(this)
        currentShownDay = Calendar.getInstance()
        TODAY = Calendar.getInstance()
        MAX_FUTURE_DAY = Calendar.getInstance()
        MAX_FUTURE_DAY.add(Calendar.DAY_OF_YEAR, 7)
    }

    override fun onPrevPressed() {
        currentShownDay.add(Calendar.DAY_OF_YEAR, -1)
        getPoolClasses(currentShownDay)
    }

    override fun onTodayPressed() {
        getPoolClasses(todayString)
    }

    override fun onNextPressed() {
        currentShownDay.add(Calendar.DAY_OF_YEAR, 1)
        getPoolClasses(currentShownDay)
    }

    override fun onViewCreated() {
        getPoolClasses(todayString)
    }

    private fun getPoolClasses(date: Calendar) {
        view.disableAllRefreshButtons()
        view.hideErrorMessage()
        view.showLoadingIndicator()
        view.updateSchedule(ArrayList())
        setDateText(date)
        interactor.getGymEventsObservable(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ receivedGymEvents ->
                    gymEvents = receivedGymEvents
                    view.updateSchedule(getVisibleEvents(gymEvents))
                    view.hideLoadingIndicator()
                    enableDateButtons(currentShownDay)
                }, { error ->
                    Log.d(TAG, "failed to retrieve schedule", error)
                    view.hideLoadingIndicator()
                    view.showErrorMessage("Could not retrieve schedule", error)
                    view.disableNextButton()
                    view.disablePrevButton()
                    view.enableTodayButton()
                })
    }

    private fun setDateText(date: Calendar) {
        view.setDate(DISPLAYED_DATE_FORMAT.format(date.time))
    }

    private fun enableDateButtons(shownDate: Calendar) {
        if (dateLessThanOrEqualTo(shownDate, TODAY)) {
            view.disablePrevButton()
        } else {
            view.enablePrevButton()
        }

        if (datesOnSameDay(shownDate, TODAY)) {
            view.disableTodayButton()
        } else {
            view.enableTodayButton()
        }

        if (dateGreaterThanOrEqualTo(shownDate, MAX_FUTURE_DAY)) {
            view.disableNextButton()
        } else {
            view.enableNextButton()
        }
    }

    private fun dateLessThanOrEqualTo(first: Calendar, second: Calendar): Boolean {
        return first.get(Calendar.YEAR) < second.get(Calendar.YEAR)
                || first.get(Calendar.DAY_OF_YEAR) <= second.get(Calendar.DAY_OF_YEAR)
    }

    private fun dateGreaterThanOrEqualTo(first: Calendar, second: Calendar): Boolean {
        return first.get(Calendar.YEAR) >= second.get(Calendar.YEAR)
                && first.get(Calendar.DAY_OF_YEAR) >= second.get(Calendar.DAY_OF_YEAR)
    }

    private fun datesOnSameDay(first: Calendar, second: Calendar): Boolean {
        return first.get(Calendar.YEAR) == second.get(Calendar.YEAR)
                && first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR)
    }

    // TODO placeholder code
    private var poolIsChecked = false
    override fun isEventCategoryChecked(pooL_ACTIVITIES: EventType): Boolean {
        return poolIsChecked
    }

    override fun onEventCategoryToggled(checked: Boolean, pooL_ACTIVITIES: EventType) {
        poolIsChecked = checked
        view.updateSchedule(getVisibleEvents(gymEvents))
    }

    private fun getVisibleEvents(gymEvents: List<GymEvent>) : List<GymEvent> {
        if (!poolIsChecked) {
            return gymEvents
        }
        return gymEvents.filter { it -> it.eventType == EventType.POOL_ACTIVITIES }
    }
}
