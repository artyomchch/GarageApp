package kozlov.artyom.garageapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kozlov.artyom.garageapp.data.database.CarListDao
import kozlov.artyom.garageapp.data.mapper.CarListMapper
import kozlov.artyom.garageapp.domain.entity.CarItem
import kozlov.artyom.garageapp.domain.repository.CarListRepository
import javax.inject.Inject

class CarListRepositoryImpl @Inject constructor(
    private val mapper: CarListMapper,
    private val carListDao: CarListDao
) : CarListRepository {


    override suspend fun editCarItem(carItem: CarItem) {
        carListDao.addCarItem(mapper.mapEntityToDbModel(carItem))
    }

    override suspend fun addCarItem(carItem: CarItem) {
        carListDao.addCarItem(mapper.mapEntityToDbModel(carItem))
    }

    override suspend fun getCarItem(carItemId: Int): CarItem {
        val dbModel = carListDao.getCarItem(carItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override suspend fun deleteCarItem(carItem: CarItem) {
        carListDao.deleteCarItem(carItem.id)
    }

    override fun getCarList(): LiveData<List<CarItem>> = Transformations.map(carListDao.getCarList()) {
        mapper.mapListDbModelToListEntity(it)
    }


}