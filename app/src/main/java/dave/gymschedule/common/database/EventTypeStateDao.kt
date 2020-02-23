package dave.gymschedule.common.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface EventTypeStateDao {

    @Query("SELECT * FROM EventTypeState")
    fun getAllEventTypeStates(): Observable<List<EventTypeState>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateEventTypeState(eventTypeState: EventTypeState): Completable

}