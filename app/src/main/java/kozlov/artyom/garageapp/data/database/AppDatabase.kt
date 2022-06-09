package kozlov.artyom.garageapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CarItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun carListDao(): CarListDao

    companion object {

        private var db: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "car_item.db"


        fun getInstance(context: Context): AppDatabase {
            synchronized(LOCK) {
                db?.let {
                    return it
                }
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                db = instance
                return instance
            }
        }
    }
}