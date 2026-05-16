package com.vestis.data.profile.di

import com.vestis.data.profile.repository.ProfileRepositoryImpl
import com.vestis.domain.profile.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepository: ProfileRepositoryImpl
    ): ProfileRepository
}