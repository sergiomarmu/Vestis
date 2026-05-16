package com.vestis.data.profile.network.di

import com.vestis.core.data.network.retrofit.RetrofitFactory
import com.vestis.core.data.network.retrofit.di.RetrofitModule
import com.vestis.data.profile.network.api.ProfileApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileNetworkApiModule {

    private const val BASE_URL = "https://fakestoreapi.com"

    @Provides
    @Singleton
    fun provideProfileApiService(
        factory: RetrofitFactory,
        okHttp: OkHttpClient,
        @Named(value = RetrofitModule.JSON_KOTLIN_SERIALIZER_FACTORY) converterFactory: Converter.Factory
    ): ProfileApiService = factory.create(
        baseUrl = BASE_URL,
        okHttpClient = okHttp,
        converterFactory = converterFactory
    ).create(ProfileApiService::class.java)
}