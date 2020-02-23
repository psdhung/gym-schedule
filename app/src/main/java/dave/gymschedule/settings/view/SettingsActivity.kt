package dave.gymschedule.settings.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerAppCompatActivity
import dave.gymschedule.R
import dave.gymschedule.common.database.GymLocationRepository
import dave.gymschedule.common.model.EventType
import dave.gymschedule.settings.model.GymLocation
import dave.gymschedule.settings.presenter.SettingsPresenter
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

class SettingsActivity : DaggerAppCompatActivity() {

    companion object {
        private val TAG = SettingsActivity::class.java.simpleName
    }

    @Inject
    lateinit var presenter: SettingsPresenter

    @Inject
    lateinit var gymLocationRepository: GymLocationRepository

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.title = getString(R.string.settings_activity_title)

        event_checkbox_recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = EventTypeCheckboxAdapter { eventType, isChecked ->
            Log.d("SettingsAdapter", "changing $eventType to $isChecked")
            disposables.add(presenter.onEventTypeToggled(eventType, isChecked)
                    .subscribeOn(io())
                    .observeOn(io())
                    .subscribe({
                        Log.d(TAG, "finished updating database")
                    }, {
                        Log.d(TAG, "failed to update database", it)
                    })
            )
        }
        event_checkbox_recyclerview.adapter = adapter
        disposables.add(presenter.getEventTypesMapObservable()
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe({ eventTypesMap ->
                    Log.d(TAG, "got event type states: $eventTypesMap")
                    val eventTypes = mutableListOf<Pair<EventType, Boolean>>()
                    for (entry in EventType.values()) {
                        if (entry == EventType.OTHER) {
                            continue
                        }
                        val enabled = eventTypesMap[entry.eventTypeId] ?: false
                        eventTypes.add(Pair(EventType.getEventTypeFromId(entry.eventTypeId), enabled))
                    }

                    Log.d(TAG, "setting adapter with event types: $eventTypes")
                    adapter.setData(eventTypes)
                }, {
                    Log.d(TAG, "failed to get event type states from repository", it)
                }))

        val gymLocationAdapter = GymLocationAdapter(this, GymLocation.values().asList())
        gym_location_spinner.adapter = gymLocationAdapter
        gym_location_spinner.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                disposables.add(gymLocationRepository.setSavedGymLocationId(gymLocationAdapter.getLocationId(position))
                        .subscribeOn(io())
                        .observeOn(mainThread())
                        .subscribe({

                        }, {

                        })
                )
            }
        }

        gym_location_spinner.setSelection(GymLocation.getGymLocationByLocationId(gymLocationRepository.getSavedGymLocationId()).ordinal)
    }

}

