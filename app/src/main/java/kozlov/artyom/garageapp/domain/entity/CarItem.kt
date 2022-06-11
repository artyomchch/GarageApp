package kozlov.artyom.garageapp.domain.entity

data class CarItem(
    val pathToPic: String,
    val name: String,
    val model: String,
    val cylinderVolume: String,
    val power: String,
    val fuel: String,
    val driveUnit: String,
    val color: String,
    val price: String,
    var id: Int = UNDEFINED_ID
){
    companion object{
        const val UNDEFINED_ID = 0
    }
}
