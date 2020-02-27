package dave.gymschedule.common.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EventFilter::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventTypeFilterDao(): EventFilterDao
}