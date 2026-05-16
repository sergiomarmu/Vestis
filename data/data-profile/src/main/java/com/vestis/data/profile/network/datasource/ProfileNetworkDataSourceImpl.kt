package com.vestis.data.profile.network.datasource

import com.vestis.core.common.dispatcher.IoDispatcher
import com.vestis.core.data.network.handler.tryRequest
import com.vestis.data.profile.network.api.ProfileApiService
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ProfileNetworkDataSourceImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val profileApiService: ProfileApiService,
) : ProfileNetworkDataSource {

    override suspend fun getProfile() = tryRequest(
        ioDispatcher = ioDispatcher
    ) {
        profileApiService.getUser()
    }
}