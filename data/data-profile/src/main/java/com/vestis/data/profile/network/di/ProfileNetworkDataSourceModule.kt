package com.vestis.data.profile.network.di

import com.vestis.data.profile.network.datasource.ProfileNetworkDataSource
import com.vestis.data.profile.network.datasource.ProfileNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileNetworkDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindProfileNetworkDataSource(
        profileNetworkDataSource: ProfileNetworkDataSourceImpl
    ): ProfileNetworkDataSource
}