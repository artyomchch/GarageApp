package kozlov.artyom.garageapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_items")
data class CarItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val brand: String,
    val model: String,
    val enable: Boolean,
)
