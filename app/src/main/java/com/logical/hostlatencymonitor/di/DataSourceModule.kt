package com.logical.hostlatencymonitor.di

import com.google.gson.Gson
import com.logical.hostlatencymonitor.data.datasource.HostDataSource
import com.logical.hostlatencymonitor.data.datasource.HostDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindHostDataSource(
        hostDataSourceImpl: HostDataSourceImpl
    ): HostDataSource

    companion object {
        @Provides
        @Singleton
        fun provideGson(): Gson {
            return Gson()
        }
    }
}