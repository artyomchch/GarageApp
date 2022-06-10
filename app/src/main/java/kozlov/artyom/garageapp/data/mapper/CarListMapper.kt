package kozlov.artyom.garageapp.data.mapper

import kozlov.artyom.garageapp.data.database.CarItemDbModel
import kozlov.artyom.garageapp.domain.entity.CarItem
import javax.inject.Inject

class CarListMapper @Inject constructor() {

    fun mapEntityToDbModel(carItem: CarItem) = CarItemDbModel(
        id = carItem.id,
        name = carItem.name,
        model = carItem.model,
        cylinderVolume = carItem.cylinderVolume,
        pathToPic = carItem.pathToPic,
        power = carItem.power,
        fuel = carItem.fuel,
        driveUnit = carItem.driveUnit,
        color = carItem.color,
        price = carItem.price
    )


    fun mapDbModelToEntity(carItemDbModel: CarItemDbModel) = CarItem(
        id = carItemDbModel.id,
        name = carItemDbModel.name,
        model = carItemDbModel.model,
        cylinderVolume = carItemDbModel.cylinderVolume,
        pathToPic = carItemDbModel.pathToPic,
        power = carItemDbModel.power,
        fuel = carItemDbModel.fuel,
        driveUnit = carItemDbModel.driveUnit,
        color = carItemDbModel.color,
        price = carItemDbModel.price

    )

    fun mapListDbModelToListEntity(list: List<CarItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}