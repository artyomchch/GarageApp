package kozlov.artyom.garageapp.presentation.secondfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kozlov.artyom.garageapp.domain.entity.CarItem
import kozlov.artyom.garageapp.domain.usecases.AddCarItemUseCase
import kozlov.artyom.garageapp.domain.usecases.EditCarItemUseCase
import kozlov.artyom.garageapp.domain.usecases.GetCarItemUseCase
import javax.inject.Inject

class CarItemViewModel @Inject constructor(
    private val getCarItemUseCase: GetCarItemUseCase,
    private val addCarItemUseCase: AddCarItemUseCase,
    private val editCarItemUseCase: EditCarItemUseCase,
) : ViewModel() {


    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputBrand = MutableLiveData<Boolean>()
    val errorInputBrand: LiveData<Boolean>
        get() = _errorInputBrand

    private val _errorInputModel = MutableLiveData<Boolean>()
    val errorInputModel: LiveData<Boolean>
        get() = _errorInputModel

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    private val _carItem = MutableLiveData<CarItem>()
    val carItem: LiveData<CarItem>
        get() = _carItem


    fun getCarItem(carItemId: Int) {
        viewModelScope.launch {
            val item = getCarItemUseCase(carItemId)
            _carItem.value = item
        }
    }

    fun addCarItem(inputName: String?, inputBrand: String?, inputModel: String?) {
        val name = parseName(inputName)
        val brand = parseName(inputBrand)
        val model = parseName(inputModel)
        val fieldsValid = validateInput(name, brand, model)
        if (fieldsValid) {
            viewModelScope.launch {
                val carItem = CarItem(name, brand, model, 2, 249, "Diesel", "4WD", "blue", 12)
                addCarItemUseCase(carItem)
                finishWork()
            }

        }
    }

    fun editCarItem(inputName: String?, inputBrand: String?, inputModel: String?) {
        val name = parseName(inputName)
        val brand = parseName(inputBrand)
        val model = parseName(inputModel)
        val fieldsValid = validateInput(name, brand, model)
        if (fieldsValid) {
            _carItem.value?.let {
                viewModelScope.launch {
                    val item = it.copy(name = name, color = brand, model = model)
                    editCarItemUseCase(item)
                    finishWork()
                }
            }
        }

    }


    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputBrand() {
        _errorInputBrand.value = false
    }

    fun resetErrorInputModel() {
        _errorInputModel.value = false
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun validateInput(name: String, brand: String, model: String): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (brand.isBlank()) {
            _errorInputBrand.value = true
            result = false
        }
        if (model.isBlank()) {
            _errorInputModel.value = true
            result = false
        }
        return result
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

}