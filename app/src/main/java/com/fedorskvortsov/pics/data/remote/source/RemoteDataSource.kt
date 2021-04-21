package com.fedorskvortsov.pics.data.remote.source

import com.fedorskvortsov.pics.data.remote.api.ImageService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: ImageService
) {
    val images = flow {
        val response = service.getImages()

        if (!response.isSuccessful) {
            throw IllegalStateException("TODO")
        }

        emit(response.body() ?: emptyList<String>())
    }
}
