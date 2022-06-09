package kozlov.artyom.garageapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ScreenAddMode : Parcelable {
    EDIT, ADD
}