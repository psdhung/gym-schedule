package dave.gymschedule

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dave.gymschedule.model.GymEvent
import java.util.ArrayList

class GymEventAdapter(gymEvents: List<GymEvent>) : RecyclerView.Adapter<GymEventAdapter.GymEventViewHolder>() {
    companion object {

        private const val LANE_SWIM_NAME = "Lane Swim"
    }

    private val gymEvents: List<GymEvent> = ArrayList(gymEvents)

    class GymEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var view: View = itemView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GymEventViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.gym_event_view_holder, parent, false)
        return GymEventViewHolder(layout)
    }

    override fun onBindViewHolder(holder: GymEventViewHolder, position: Int) {
        val event = gymEvents[position]
        (holder.view.findViewById<View>(R.id.name) as TextView).text = event.name

        val detailsTextView = holder.view.findViewById<TextView>(R.id.details)
        val details = event.details
        if (details.isNotEmpty()) {
            detailsTextView.text = details
            detailsTextView.visibility = View.VISIBLE
        } else {
            detailsTextView.visibility = View.GONE
        }

        (holder.view.findViewById<View>(R.id.start_time) as TextView).text = event.startTime

        (holder.view.findViewById<View>(R.id.end_time) as TextView).text = event.endTime

        if (LANE_SWIM_NAME.equals(event.name, ignoreCase = true)) {
            holder.view.setBackgroundResource(R.drawable.bg_highlighted_event)
        } else {
            holder.view.background = null
        }
    }

    override fun getItemCount(): Int {
        return gymEvents.size
    }

}
