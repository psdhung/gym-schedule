package dave.gymschedule

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import dave.gymschedule.model.GymEventViewModel
import java.util.ArrayList

class GymEventAdapter(context: Context, gymEvents: List<GymEventViewModel>) : RecyclerView.Adapter<GymEventAdapter.GymEventViewHolder>() {
    companion object {

        private const val LANE_SWIM_NAME = "Lane Swim"
    }

    private val gymEvents: List<GymEventViewModel> = ArrayList(gymEvents)

    private val defaultCardBackground = ColorDrawable(ContextCompat.getColor(context, R.color.event_card_bg_color))

    class GymEventViewHolder(itemView: CardView) : RecyclerView.ViewHolder(itemView) {
        internal var view: View = itemView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GymEventViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.gym_event_entry, parent, false) as CardView
        return GymEventViewHolder(layout)
    }

    override fun onBindViewHolder(holder: GymEventViewHolder, position: Int) {
        val event = gymEvents[position]
        (holder.view.findViewById<View>(R.id.event_name) as TextView).text = event.name

        val detailsTextView = holder.view.findViewById<TextView>(R.id.event_details)
        val details = event.details
        if (details?.isNotEmpty() == true) {
            detailsTextView.text = details
            detailsTextView.visibility = View.VISIBLE
        } else {
            detailsTextView.visibility = View.GONE
        }

        (holder.view.findViewById<View>(R.id.event_start_time) as TextView).text = event.startTime

        (holder.view.findViewById<View>(R.id.event_end_time) as TextView).text = event.endTime

        if (LANE_SWIM_NAME.equals(event.name, ignoreCase = true)) {
            holder.view.setBackgroundResource(R.drawable.bg_highlighted_event)
        } else {
            (holder.view as CardView).background = defaultCardBackground
        }
    }

    override fun getItemCount(): Int {
        return gymEvents.size
    }

}
