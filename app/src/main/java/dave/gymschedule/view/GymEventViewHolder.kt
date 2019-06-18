package dave.gymschedule.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import dave.gymschedule.R

class GymEventViewHolder(val view: MaterialCardView) : RecyclerView.ViewHolder(view) {

    private val expandedSection = view.findViewById<ViewGroup>(R.id.event_expanded_section)

    fun setEventName(eventName: String) {
        view.findViewById<TextView>(R.id.event_name).text = eventName
    }

    fun setEventLocation(eventLocation: String) {
        view.findViewById<TextView>(R.id.event_location).text = eventLocation
    }

    fun setEventStartTime(eventStartTime: String) {
        view.findViewById<TextView>(R.id.event_start_time).text = eventStartTime
    }

    fun setEventEndTime(eventEndTime: String) {
        view.findViewById<TextView>(R.id.event_end_time).text = eventEndTime
    }

    fun setEventDetails(eventDetails: String?) {
        val eventDetailsTextView = view.findViewById<TextView>(R.id.event_details)
        if (eventDetails?.isEmpty() == true || eventDetails?.isBlank() == true) {
            eventDetailsTextView.visibility = View.GONE
        } else {
            eventDetailsTextView.text = eventDetails
            eventDetailsTextView.visibility = View.VISIBLE
        }
    }

    fun setEventDescription(eventDescription: String) {
        val eventDescriptionWrapper = view.findViewById<ViewGroup>(R.id.event_description_wrapper)
        if (eventDescription.isEmpty() || eventDescription.isBlank()) {
            eventDescriptionWrapper.visibility = View.GONE
        } else {
            view.findViewById<TextView>(R.id.event_description).text = eventDescription
            eventDescriptionWrapper.visibility = View.VISIBLE
        }
    }

    fun setEventFee(eventFee: String) {
        val eventFeeWrapper = view.findViewById<ViewGroup>(R.id.event_fee_wrapper)
        if (eventFee.isEmpty() || eventFee.isBlank()) {
            eventFeeWrapper.visibility = View.GONE
        } else {
            view.findViewById<TextView>(R.id.event_fee).text = eventFee
            eventFeeWrapper.visibility = View.VISIBLE
        }
    }

    fun setAgeRange(ageRange: String) {
        view.findViewById<TextView>(R.id.event_age_range).text = ageRange
    }

    fun setEventRegistration(eventRegistration: String) {
        view.findViewById<TextView>(R.id.event_registration).text = eventRegistration
    }

    fun setHasChildMinding(hasChildMinding: Boolean) {
        view.findViewById<TextView>(R.id.event_has_child_minding).visibility = if (hasChildMinding) View.VISIBLE else View.GONE
    }

    fun showExpandedSection() {
        expandedSection.visibility = View.VISIBLE
    }

    fun hideExpandedSection() {
        expandedSection.visibility = View.GONE
    }

    fun isCurrentlyExpanded(): Boolean {
        return expandedSection.isVisible
    }

}