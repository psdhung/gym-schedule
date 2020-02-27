package dave.gymschedule.common.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface EventFilterDao {

    @Query("SELECT * FROM EventFilter")
    fun getAllEventFilters(): Observable<List<EventFilter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateEventFilter(eventFilter: EventFilter): Completable

}