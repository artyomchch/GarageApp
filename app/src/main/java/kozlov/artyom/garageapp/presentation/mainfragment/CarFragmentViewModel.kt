package kozlov.artyom.garageapp.presentation.mainfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kozlov.artyom.garageapp.domain.entity.CarItem
import kozlov.artyom.garageapp.domain.usecases.DeleteCarItemUseCase
import kozlov.artyom.garageapp.domain.usecases.EditCarItemUseCase
import kozlov.artyom.garageapp.domain.usecases.GetCarListUseCase
import javax.inject.Inject


class CarFragmentViewModel @Inject constructor(
    getCarListUseCase: GetCarListUseCase,
    private val deleteCarItemUseCase: DeleteCarItemUseCase,
    private val editCarItemUseCase: EditCarItemUseCase,
) : ViewModel() {


    val carList = getCarListUseCase.invoke()





    fun deleteCarItem(carItem: CarItem) {
        viewModelScope.launch {
            deleteCarItemUseCase(carItem)
        }

    }

}