package dave.gymschedule.settings.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import dave.gymschedule.R
import dave.gymschedule.settings.model.GymLocation

class GymLocationAdapter(private val context: Context, private val gymLocations: List<GymLocation>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.gym_location, parent, false)
        val gymLocation = gymLocations[position]

        view.findViewById<TextView>(R.id.location_name).setText(gymLocation.locationName)
        view.findViewById<TextView>(R.id.location_address).setText(gymLocation.locationAddress)

        return view
    }

    override fun getItem(position: Int): Any {
        return gymLocations[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return gymLocations.size
    }

}