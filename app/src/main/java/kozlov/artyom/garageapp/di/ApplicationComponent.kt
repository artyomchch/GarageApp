package kozlov.artyom.garageapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import kozlov.artyom.garageapp.presentation.mainfragment.CarFragment
import kozlov.artyom.garageapp.presentation.secondfragment.CarItemFragment


@ApplicationScope
@Component(
    modules = [ViewModelModule::class, CarsDomainModule::class]
)
interface ApplicationComponent {

    fun inject(fragment: CarFragment)

    fun inject(fragment: CarItemFragment)


    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}