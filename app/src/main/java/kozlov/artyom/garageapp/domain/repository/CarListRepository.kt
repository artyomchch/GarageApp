package kozlov.artyom.garageapp.domain.repository

import androidx.lifecycle.LiveData
import kozlov.artyom.garageapp.domain.entity.CarItem

interface CarListRepository {

    suspend fun editCarItem(carItem: CarItem)

    suspend fun addCarItem(carItem: CarItem)

    suspend fun getCarItem(carItemId: Int): CarItem

    suspend fun deleteCarItem(carItem: CarItem)

    fun getCarList(): LiveData<List<CarItem>>

}