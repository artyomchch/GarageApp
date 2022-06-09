package kozlov.artyom.garageapp.data.mapper

import kozlov.artyom.garageapp.data.database.CarItemDbModel
import kozlov.artyom.garageapp.domain.entity.CarItem
import javax.inject.Inject

class CarListMapper @Inject constructor() {

    fun mapEntityToDbModel(carItem: CarItem) = CarItemDbModel(
        id = carItem.id,
        name = carItem.name,
        brand = carItem.brand,
        model = carItem.model,
        enable = carItem.enable
    )

    fun mapDbModelToEntity(carItemDbModel: CarItemDbModel) = CarItem(
        id = carItemDbModel.id,
        name = carItemDbModel.name,
        brand = carItemDbModel.brand,
        model = carItemDbModel.model,
        enable = carItemDbModel.enable
    )

    fun mapListDbModelToListEntity(list: List<CarItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}