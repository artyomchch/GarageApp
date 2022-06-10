package kozlov.artyom.garageapp.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import kozlov.artyom.garageapp.data.database.AppDatabase
import kozlov.artyom.garageapp.data.database.CarListDao
import kozlov.artyom.garageapp.domain.repository.CarListRepository
import kozlov.artyom.garageapp.data.repository.CarListRepositoryImpl

@Module
interface CarsDomainModule {

    @ApplicationScope
    @Binds
    fun bindCarListRepository(impl: CarListRepositoryImpl): CarListRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideAppDatabase(context: Context): CarListDao {
            return AppDatabase.getInstance(context).carListDao()
        }
    }

}