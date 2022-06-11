package kozlov.artyom.garageapp.presentation.mainfragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    fun sortByAlphabet(){
        _carListSort.value = carList.value?.sortedBy { it.name }
        Log.d("TAG", ": ${carListSort.value}")
      //  carList.value = carList.value?.sortedBy { it.name }
//        val v = carList.value
//        v?.sortedBy { it.name }
//        carList.value =
    }

}