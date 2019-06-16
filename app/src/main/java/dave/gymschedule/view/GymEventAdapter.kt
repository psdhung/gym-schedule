package dave.gymschedule.view

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import dave.gymschedule.R
import dave.gymschedule.model.GymEventViewModel

class GymEventAdapter(private val gymEvents: List<GymEventViewModel>) : RecyclerView.Adapter<GymEventAdapter.GymEventViewHolder>() {

    class GymEventViewHolder(val view: CardView) : RecyclerView.ViewHolder(view)

    companion object {
        private val TAG = GymEventAdapter::class.java.simpleName

        private const val LANE_SWIM_NAME = "Lane Swim"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GymEventViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.gym_event_entry, parent, false) as CardView
        return GymEventViewHolder(layout)
    }

    override fun onBindViewHolder(holder: GymEventViewHolder, position: Int) {
        val gymEvent = gymEvents[position]

        holder.view.findViewById<TextView>(R.id.event_name).text = gymEvent.name
        holder.view.findViewById<TextView>(R.id.event_location).text = gymEvent.location
        holder.view.findViewById<TextView>(R.id.event_start_time).text = gymEvent.startTime
        holder.view.findViewById<TextView>(R.id.event_end_time).text = gymEvent.endTime

        if (LANE_SWIM_NAME.equals(gymEvent.name, ignoreCase = true)) {
            holder.view.setBackgroundResource(R.drawable.bg_highlighted_event)
        } else {
            holder.view.background = ColorDrawable(ContextCompat.getColor(holder.view.context, R.color.event_card_bg_color))
        }

        val detailsTextView = holder.view.findViewById<TextView>(R.id.event_details)
        val details = gymEvent.details
        if (details?.isNotEmpty() == true) {
            detailsTextView.text = details
            detailsTextView.visibility = View.VISIBLE
        } else {
            detailsTextView.visibility = View.GONE
        }

        val expandedSection = holder.view.findViewById<LinearLayout>(R.id.event_expanded_section)
        Log.d(TAG, "event at position $position is expanded? ${gymEvent.isExpanded}")
        expandedSection.visibility = if (gymEvent.isExpanded) View.VISIBLE else View.GONE

        holder.view.setOnClickListener {
            // TODO animation
            val isCurrentlyExpanded = expandedSection.isVisible
            expandedSection.visibility = if (isCurrentlyExpanded) View.GONE else View.VISIBLE
            gymEvent.isExpanded = !isCurrentlyExpanded
            Log.d(TAG, "toggled event at position $position to ${if (gymEvent.isExpanded) "" else "not "}expanded")
        }

        val eventDescription = gymEvent.description
        val eventDescriptionWrapper = holder.view.findViewById<ViewGroup>(R.id.event_description_wrapper)
        if (eventDescription.isEmpty() || eventDescription.isBlank()) {
            eventDescriptionWrapper.visibility = View.GONE
        } else {
            eventDescriptionWrapper.visibility = View.VISIBLE
            holder.view.findViewById<TextView>(R.id.event_description).text = eventDescription
        }

        val eventFee = gymEvent.fee
        val eventFeeWrapper = holder.view.findViewById<ViewGroup>(R.id.event_fee_wrapper)
        if (eventFee.isEmpty() || eventFee.isBlank()) {
            eventFeeWrapper.visibility = View.GONE
        } else {
            eventFeeWrapper.visibility = View.VISIBLE
            holder.view.findViewById<TextView>(R.id.event_fee).text = eventFee
        }

        holder.view.findViewById<TextView>(R.id.event_age_range).text = gymEvent.ageRange
        holder.view.findViewById<TextView>(R.id.event_registration).text = gymEvent.registration

        holder.view.findViewById<TextView>(R.id.event_has_child_minding).visibility = if (gymEvent.childMinding) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int {
        return gymEvents.size
    }

}
