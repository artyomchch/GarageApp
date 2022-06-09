package kozlov.artyom.garageapp.domain.usecases

import kozlov.artyom.garageapp.domain.repository.CarListRepository
import javax.inject.Inject

class GetCarItemUseCase @Inject constructor(private val carListRepository: CarListRepository) {

    suspend operator fun invoke(carItemId: Int) = carListRepository.getCarItem(carItemId)

}