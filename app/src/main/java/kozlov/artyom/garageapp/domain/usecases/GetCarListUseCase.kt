package kozlov.artyom.garageapp.domain.usecases

import androidx.lifecycle.LiveData
import kozlov.artyom.garageapp.domain.entity.CarItem
import kozlov.artyom.garageapp.domain.repository.CarListRepository
import javax.inject.Inject

class GetCarListUseCase @Inject constructor(private val carListRepository: CarListRepository) {

    operator fun invoke(): LiveData<List<CarItem>> = carListRepository.getCarList()

}