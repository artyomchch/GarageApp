package kozlov.artyom.garageapp.presentation.mainfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kozlov.artyom.garageapp.domain.entity.CarItem
import kozlov.artyom.garageapp.domain.usecases.DeleteCarItemUseCase
import kozlov.artyom.garageapp.domain.usecases.GetCarListUseCase
import javax.inject.Inject


class CarFragmentViewModel @Inject constructor(
    getCarListUseCase: GetCarListUseCase,
    private val deleteCarItemUseCase: DeleteCarItemUseCase,
) : ViewModel() {

    private val _carListSort = MutableLiveData<List<CarItem>>()
    val carListSort: LiveData<List<CarItem>>
        get() = _carListSort

    val carList = getCarListUseCase.invoke()



    fun deleteCarItem(carItem: CarItem) {
        viewModelScope.launch {
            deleteCarItemUseCase(carItem)
        }

    }



    fun sortByAlphabet() {
        if (triggerValue) {
            _carListSort.value = carList.value?.sortedBy { it.name }
            triggerValue = false
        } else {
            _carListSort.value = carList.value?.sortedByDescending { it.name }
            triggerValue = true
        }
    }

    companion object{
       private var triggerValue = true
    }

}