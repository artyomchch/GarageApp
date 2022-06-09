package kozlov.artyom.garageapp.domain.usecases

import kozlov.artyom.garageapp.domain.entity.CarItem
import kozlov.artyom.garageapp.domain.repository.CarListRepository
import javax.inject.Inject

class AddCarItemUseCase @Inject constructor(private val carListRepository: CarListRepository) {

    suspend operator fun invoke(carItem: CarItem) {
        carListRepository.addCarItem(carItem)
    }

}