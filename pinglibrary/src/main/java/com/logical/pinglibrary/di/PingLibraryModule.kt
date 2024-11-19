package com.logical.pinglibrary.di

import com.logical.pinglibrary.data.datasource.PingDataSource
import com.logical.pinglibrary.data.datasource.PingDataSourceImpl
import com.logical.pinglibrary.data.repository.PingRepositoryImpl
import com.logical.pinglibrary.domain.repository.PingRepository
import com.logical.pinglibrary.domain.usecase.PingHostUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PingLibraryModule {

    // Provide the PingDataSource implementation
    @Provides
    @Singleton
    fun providePingDataSource(): PingDataSource {
        return PingDataSourceImpl()
    }

    // Provide the PingRepository implementation
    @Provides
    @Singleton
    fun providePingRepository(dataSource: PingDataSource): PingRepository {
        return PingRepositoryImpl(dataSource)
    }

    // Provide the PingHostUseCase
    @Provides
    @Singleton
    fun providePingHostUseCase(repository: PingRepository): PingHostUseCase {
        return PingHostUseCase(repository)
    }
}
