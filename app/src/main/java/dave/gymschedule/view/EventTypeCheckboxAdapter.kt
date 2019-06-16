package dave.gymschedule.view

import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import dave.gymschedule.model.EventType

class EventTypeCheckboxAdapter(private val onCheckChangedListener: (eventType: EventType, isChecked: Boolean) -> Unit) : RecyclerView.Adapter<EventTypeCheckboxAdapter.EventTypeCheckboxViewHolder>() {

    class EventTypeCheckboxViewHolder(val item: CheckBox) : RecyclerView.ViewHolder(item)

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
