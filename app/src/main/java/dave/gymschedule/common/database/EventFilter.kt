package dave.gymschedule.common.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EventFilter(
        @PrimaryKey
        var eventTypeId: Int,

        var enabled: Boolean
)