package dave.gymschedule.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable

@Dao
interface EventTypeStateDao {

    @Query("SELECT * FROM EventTypeState")
    fun getAllEventTypeStates(): LiveData<List<EventTypeState>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateEventTypeState(eventTypeState: EventTypeState): Completable

}