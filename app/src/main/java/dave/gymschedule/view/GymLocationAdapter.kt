package dave.gymschedule.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import dave.gymschedule.R
import dave.gymschedule.model.GymLocation

class GymLocationAdapter(private val context: Context, private val gymLocations: List<GymLocation>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var localConvertView = convertView
        if (localConvertView == null) {
            localConvertView = LayoutInflater.from(context).inflate(R.layout.gym_location, parent, false)
        }

        val gymLocation = gymLocations[position]

        localConvertView!!.findViewById<TextView>(R.id.location_name).setText(gymLocation.locationName)
        localConvertView.findViewById<TextView>(R.id.location_address).setText(gymLocation.locationAddress)

        return localConvertView
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

    fun getLocationId(position: Int): Int {
        return gymLocations[position].locationId
    }

}