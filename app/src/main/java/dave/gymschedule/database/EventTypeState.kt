package dave.gymschedule.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EventTypeState(
        @PrimaryKey
        val eventTypeId: Int,

        @ColumnInfo
        val checked: Boolean
)