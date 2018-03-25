package dave.gymschedule.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class EventTypeState(
        @PrimaryKey
        val eventTypeId: Int,

        @ColumnInfo
        val checked: Boolean
)