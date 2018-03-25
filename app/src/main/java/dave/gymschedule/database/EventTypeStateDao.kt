package dave.gymschedule.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface EventTypeStateDao {

    @Query("SELECT * FROM EventTypeState")
    fun getAllEventTypeStates(): List<EventTypeState>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateEventTypeState(eventTypeState: EventTypeState)

}