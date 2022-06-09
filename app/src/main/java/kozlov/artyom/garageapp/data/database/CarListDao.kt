package kozlov.artyom.garageapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CarListDao {

    @Query("SELECT * FROM car_items")
    fun getCarList(): LiveData<List<CarItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCarItem(carItemDbModel: CarItemDbModel)

    @Query("DELETE FROM car_items WHERE id=:carItemId")
    suspend fun deleteCarItem(carItemId: Int)

    @Query("SELECT * FROM car_items WHERE id=:carItemId LIMIT 1")
    suspend fun getCarItem(carItemId: Int): CarItemDbModel
}