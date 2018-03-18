package dave.gymschedule

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dave.gymschedule.Model.GymEvent
import java.util.ArrayList

class GymEventAdapter(poolClasses: List<GymEvent>) : RecyclerView.Adapter<GymEventAdapter.PoolClassViewHolder>() {
    companion object {

        private const val LANE_SWIM_NAME = "Lane Swim"
    }

    private val poolClasses: List<GymEvent>

    class PoolClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var view: View = itemView

    }

    init {
        this.poolClasses = ArrayList(poolClasses)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoolClassViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.pool_class_view_holder, parent, false)
        return PoolClassViewHolder(layout)
    }

    override fun onBindViewHolder(holder: PoolClassViewHolder, position: Int) {
        val poolClass = poolClasses[position]
        (holder.view.findViewById<View>(R.id.name) as TextView).text = poolClass.name

        val detailsTextView = holder.view.findViewById<TextView>(R.id.details)
        val details = poolClass.details
        if (details.isNotEmpty()) {
            detailsTextView.text = details
            detailsTextView.visibility = View.VISIBLE
        } else {
            detailsTextView.visibility = View.GONE
        }

        (holder.view.findViewById<View>(R.id.start_time) as TextView).text = poolClass.startTime

        (holder.view.findViewById<View>(R.id.end_time) as TextView).text = poolClass.endTime

        if (LANE_SWIM_NAME.equals(poolClass.name, ignoreCase = true)) {
            holder.view.setBackgroundResource(R.drawable.bg_lane_swim)
        } else {
            holder.view.background = null
        }
    }

    override fun getItemCount(): Int {
        return poolClasses.size
    }



}
