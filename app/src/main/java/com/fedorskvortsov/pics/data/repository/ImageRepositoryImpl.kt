package com.fedorskvortsov.pics.data.repository

import com.fedorskvortsov.pics.data.remote.source.RemoteDataSource
import com.fedorskvortsov.pics.domain.repository.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    remoteDataSource: RemoteDataSource
) : ImageRepository {
    override val images = remoteDataSource.images
}
