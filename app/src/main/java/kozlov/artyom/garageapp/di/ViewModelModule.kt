package kozlov.artyom.garageapp.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kozlov.artyom.garageapp.presentation.CarFragmentViewModel

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(CarFragmentViewModel::class)
    @Binds
    fun bindMainActivityViewModel(impl: CarFragmentViewModel): ViewModel


}