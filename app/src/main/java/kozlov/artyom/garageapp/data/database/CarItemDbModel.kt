package kozlov.artyom.garageapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kozlov.artyom.garageapp.domain.entity.CarItem

@Entity(tableName = "car_items")
data class CarItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val pathToPic: String,
    val name: String,
    val model: String,
    val cylinderVolume: Byte,
    val power: Short,
    val fuel: String,
    val driveUnit: String,
    val color: String,
    val price: Int,


)
