package dave.gymschedule.settings.view

import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import dave.gymschedule.settings.model.EventType

class EventTypeCheckboxAdapter(private val onCheckChangedListener: (eventType: EventType, isChecked: Boolean) -> Unit) : RecyclerView.Adapter<EventTypeCheckboxAdapter.EventTypeCheckboxViewHolder>() {

    class EventTypeCheckboxViewHolder(val view: CheckBox) : RecyclerView.ViewHolder(view) {
        fun setEventName(eventName: String) {
            view.text = eventName
        }

        fun setIsChecked(isChecked: Boolean) {
            view.isChecked = isChecked
        }

    }

    private var eventTypes: List<Pair<EventType, Boolean>> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventTypeCheckboxViewHolder {
        val checkbox = CheckBox(parent.context)
        checkbox.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return EventTypeCheckboxViewHolder(checkbox)
    }

    override fun getItemCount(): Int {
        return eventTypes.size
    }

    override fun onBindViewHolder(holder: EventTypeCheckboxViewHolder, position: Int) {
        val eventTypePair = eventTypes[position]
        holder.setEventName(eventTypePair.first.eventName)
        holder.setIsChecked(eventTypePair.second)
        holder.view.setOnCheckedChangeListener { _, isChecked ->
            onCheckChangedListener(eventTypePair.first, isChecked)
        }
    }

    fun setData(eventTypes: List<Pair<EventType, Boolean>>) {
        this.eventTypes = eventTypes
        notifyDataSetChanged()
    }

}
