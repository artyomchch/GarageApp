package kozlov.artyom.garageapp.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kozlov.artyom.garageapp.presentation.mainfragment.CarFragmentViewModel
import kozlov.artyom.garageapp.presentation.secondfragment.CarItemViewModel

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(CarFragmentViewModel::class)
    @Binds
    fun bindCarFragmentViewModel(impl: CarFragmentViewModel): ViewModel

    @IntoMap
    @ViewModelKey(CarItemViewModel::class)
    @Binds
    fun bindCarItemViewModel(impl: CarItemViewModel): ViewModel

}