package dave.gymschedule.schedule.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import dave.gymschedule.R
import dave.gymschedule.schedule.model.GymEventViewModel

class GymEventAdapter : RecyclerView.Adapter<GymEventViewHolder>() {

    var gymEvents: List<GymEventViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GymEventViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.gym_event_entry, parent, false) as MaterialCardView
        return GymEventViewHolder(layout)
    }

    override fun onBindViewHolder(holder: GymEventViewHolder, position: Int) {
        val gymEvent = gymEvents[position]

        holder.setEventName(gymEvent.name)
        holder.setEventLocation(gymEvent.location)
        holder.setEventStartTime(gymEvent.startTime)
        holder.setEventEndTime(gymEvent.endTime)

        holder.setEventDetails(gymEvent.details)
        holder.setEventDescription(gymEvent.description)
        holder.setEventFee(gymEvent.fee)
        holder.setAgeRange(gymEvent.ageRange)
        holder.setEventRegistration(gymEvent.registration)
        holder.setHasChildMinding(gymEvent.childMinding)

        if (gymEvent.isExpanded) {
            holder.showExpandedSection()
        } else {
            holder.hideExpandedSection()
        }

        holder.view.setOnClickListener {
            // TODO animation
            val isCurrentlyExpanded = holder.isCurrentlyExpanded()
            if (isCurrentlyExpanded) holder.hideExpandedSection() else holder.showExpandedSection()
            gymEvent.isExpanded = !isCurrentlyExpanded
        }
    }

    override fun getItemCount(): Int {
        return gymEvents.size
    }

}
