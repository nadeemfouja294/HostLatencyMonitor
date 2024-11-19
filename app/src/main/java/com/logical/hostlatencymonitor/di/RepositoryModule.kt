package com.logical.hostlatencymonitor.di

import com.logical.hostlatencymonitor.data.repository.HostRepositoryImpl
import com.logical.hostlatencymonitor.domain.repository.HostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHostRepository(
        hostRepositoryImpl: HostRepositoryImpl
    ): HostRepository
}
