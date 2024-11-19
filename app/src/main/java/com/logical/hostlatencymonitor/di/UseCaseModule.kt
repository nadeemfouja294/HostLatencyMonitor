package com.logical.hostlatencymonitor.di

import com.logical.hostlatencymonitor.domain.repository.HostRepository
import com.logical.hostlatencymonitor.domain.usecase.FetchHostsUseCase
import com.logical.pinglibrary.domain.usecase.PingHostUseCase
import com.logical.hostlatencymonitor.domain.usecase.PingHostUseCaseWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideFetchHostsUseCase(repository: HostRepository): FetchHostsUseCase {
        return FetchHostsUseCase(repository)
    }

    @Provides
    @Singleton
    fun providePingHostUseCaseWrapper(pingHostUseCase: PingHostUseCase): PingHostUseCaseWrapper {
        return PingHostUseCaseWrapper(pingHostUseCase)
    }
}