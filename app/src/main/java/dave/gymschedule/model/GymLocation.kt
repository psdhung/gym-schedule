package dave.gymschedule.model

import androidx.annotation.StringRes
import dave.gymschedule.R

enum class GymLocation(val locationId: Int, @StringRes val locationName: Int, @StringRes val locationAddress: Int) {
    CENTRAL(2, R.string.gym_location_name_central, R.string.gym_location_address_central),
    OSHAWA(4, R.string.gym_location_name_oshawa, R.string.gym_location_address_oshawa),
    BRAMPTON(38, R.string.gym_location_name_brampton, R.string.gym_location_address_brampton),
    MARKHAM(39, R.string.gym_location_name_markham, R.string.gym_location_address_markham),
    MISSISSAUGA(142, R.string.gym_location_name_mississauga, R.string.gym_location_address_mississauga),
    SCARBOROUGH(294, R.string.gym_location_name_scarborough, R.string.gym_location_address_scarborough),
    NORTH_YORK(296, R.string.gym_location_name_north_york, R.string.gym_location_address_north_york),
    WEST_END(303, R.string.gym_location_name_west_end, R.string.gym_location_address_west_end),
    DOWNTOWN(375, R.string.gym_location_name_downtown, R.string.gym_location_address_downtown);

    companion object {
        fun getGymLocationByLocationId(locationId: Int): GymLocation {
            return values().first { it.locationId == locationId }
        }
    }
}