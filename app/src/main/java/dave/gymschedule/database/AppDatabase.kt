package dave.gymschedule.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EventTypeState::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventTypeStateDao(): EventTypeStateDao
}