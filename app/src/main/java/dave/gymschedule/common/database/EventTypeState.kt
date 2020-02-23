package dave.gymschedule.common.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EventTypeState(
        @PrimaryKey
        var eventTypeId: Int,

        var enabled: Boolean
)