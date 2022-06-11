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

    private val _errorInputModel = MutableLiveData<Boolean>()
    val errorInputModel: LiveData<Boolean>
        get() = _errorInputModel

    private val _errorCountCylinder = MutableLiveData<Boolean>()
    val errorCountCylinder: LiveData<Boolean>
        get() = _errorCountCylinder

    private val _errorCountPower = MutableLiveData<Boolean>()
    val errorCountPower: LiveData<Boolean>
        get() = _errorCountPower

    private val _errorTypeFuel = MutableLiveData<Boolean>()
    val errorTypeFuel: LiveData<Boolean>
        get() = _errorTypeFuel

    private val _errorTypeUnit = MutableLiveData<Boolean>()
    val errorTypeUnit: LiveData<Boolean>
        get() = _errorTypeUnit

    private val _errorColor = MutableLiveData<Boolean>()
    val errorColor: LiveData<Boolean>
        get() = _errorColor

    private val _errorPrice = MutableLiveData<Boolean>()
    val errorPrice: LiveData<Boolean>
        get() = _errorPrice

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

    fun addCarItem(
        imageUri: String,
        inputName: String?,
        inputModel: String?,
        inputCountCylinder: String?,
        inputCountPower: String?,
        inputTypeFuel: String?,
        inputColor: String?,
        inputPrice: String?
    ) {
        val name = parseName(inputName)
        val model = parseName(inputModel)
        val countCylinder = parseName(inputCountCylinder)

        val power = parseName(inputCountPower)
        val fuel = parseName(inputTypeFuel)
        val color = parseName(inputColor)
        val price = parseName(inputPrice)

        val fieldsValid = validateInput(name, model, countCylinder,  power, fuel, color, price)
        if (fieldsValid) {
            viewModelScope.launch {
                val carItem = CarItem(
                    pathToPic = imageUri,
                    name = name,
                    model = model,
                    cylinderVolume = countCylinder,
                    power = power,
                    fuel = fuel,
                    driveUnit = "4WD",
                    color = color,
                    price = price
                )
                addCarItemUseCase(carItem)
                finishWork()
            }

        }
    }

    fun editCarItem(
        imageUri: String,
        inputName: String?,
        inputModel: String?,
        inputCountCylinder: String?,
        inputCountPower: String?,
        inputTypeFuel: String?,
        inputColor: String?,
        inputPrice: String?
    ) {
        val name = parseName(inputName)
        val countCylinder = parseName(inputCountCylinder)
        val model = parseName(inputModel)
        val power = parseName(inputCountPower)
        val fuel = parseName(inputTypeFuel)
        val color = parseName(inputColor)
        val price = parseName(inputPrice)
        val fieldsValid = validateInput(name,  model, countCylinder, power, fuel, color, price)
        if (fieldsValid) {
            _carItem.value?.let {
                viewModelScope.launch {
                    val item = it.copy(
                        pathToPic = imageUri,
                        name = name,
                        model = model,
                        cylinderVolume = countCylinder,
                        power = power,
                        fuel = fuel,
                        color = color,
                        price = price
                    )
                    editCarItemUseCase(item)
                    finishWork()
                }
            }
        }

    }


    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorCountCylinder() {
        _errorCountCylinder.value = false
    }

    fun resetErrorInputModel() {
        _errorInputModel.value = false
    }

    fun resetPowerInput() {
        _errorCountPower.value = false
    }

    fun resetFuelInput() {
        _errorTypeFuel.value = false
    }

    fun resetInputColor() {
        _errorColor.value = false
    }

    fun resetInputPrice() {
        _errorPrice.value = false
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun validateInput(name: String, model: String, cylinder: String, power: String, fuel: String, color: String, price: String): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (model.isBlank()) {
            _errorInputModel.value = true
            result = false
        }
        if (cylinder.isBlank()) {
            _errorCountCylinder.value = true
            result = false
        }
        if (power.isBlank()) {
            _errorCountPower.value = true
            result = false
        }
        if (fuel.isBlank()) {
            _errorTypeFuel.value = true
            result = false
        }
        if (color.isBlank()) {
            _errorColor.value = true
            result = false
        }
        if (price.isBlank()) {
            _errorPrice.value = true
            result = false
        }
        return result
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

}