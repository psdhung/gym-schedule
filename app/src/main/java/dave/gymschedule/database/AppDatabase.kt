package dave.gymschedule.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [EventTypeState::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventTypeStateDao(): EventTypeStateDao
}