package kozlov.artyom.garageapp.utils

import android.app.Application
import kozlov.artyom.garageapp.di.DaggerApplicationComponent

class MyApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}