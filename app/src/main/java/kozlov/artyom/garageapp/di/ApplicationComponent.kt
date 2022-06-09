package kozlov.artyom.garageapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import kozlov.artyom.garageapp.presentation.CarFragment


@ApplicationScope
@Component(
    modules = [ViewModelModule::class, CarsDomainModule::class]
)
interface ApplicationComponent {

    fun inject(fragment: CarFragment)


    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}