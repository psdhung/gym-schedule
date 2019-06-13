package dave.gymschedule

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerAppCompatActivity
import dave.gymschedule.model.EventType
import dave.gymschedule.repository.EventTypeStateRepository
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
    lateinit var eventTypeStateRepository: EventTypeStateRepository

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.title = "Settings"

        event_checkbox_recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = EventTypeCheckboxAdapter { eventType, isChecked ->
            Log.d("SettingsAdapter", "changing $eventType to $isChecked")
            disposables.add(eventTypeStateRepository.updateEventTypeState(eventType, isChecked)
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
        disposables.add(eventTypeStateRepository.eventTypeStatesPublisher
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe({ eventTypesMap ->
                    Log.d(TAG, "got event type states: $eventTypesMap")
                    val eventTypes = mutableListOf<Pair<EventType, Boolean>>()
                    for (entry in EventType.values()) {
                        val enabled = eventTypesMap[entry.eventTypeId] ?: false
                        eventTypes.add(Pair(EventType.getEventTypeFromId(entry.eventTypeId), enabled))
                    }

                    Log.d(TAG, "setting adapter with event types: $eventTypes")
                    adapter.setData(eventTypes)
                }, {
                    Log.d(TAG, "failed to get event type states from repository", it)
                }))
    }

}

class EventTypeCheckboxAdapter(private val onCheckChangedListener: (eventType: EventType, isChecked: Boolean) -> Unit) : RecyclerView.Adapter<EventTypeCheckboxAdapter.EventTypeCheckboxViewHolder>() {

    private var eventTypes: List<Pair<EventType, Boolean>> = listOf()

    class EventTypeCheckboxViewHolder(val item: CheckBox) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventTypeCheckboxViewHolder {
        val checkbox = CheckBox(parent.context)
        checkbox.layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        return EventTypeCheckboxViewHolder(checkbox)
    }

    override fun getItemCount(): Int {
        return eventTypes.size
    }

    override fun onBindViewHolder(holder: EventTypeCheckboxViewHolder, position: Int) {
        val eventTypePair = eventTypes[position]
        holder.item.text = eventTypePair.first.eventName
        holder.item.isChecked = eventTypePair.second
        holder.item.setOnCheckedChangeListener { _, isChecked ->
            onCheckChangedListener(eventTypePair.first, isChecked)
        }
    }

    fun setData(eventTypes: List<Pair<EventType, Boolean>>) {
        this.eventTypes = eventTypes
        notifyDataSetChanged()
    }

}
