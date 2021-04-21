package com.fedorskvortsov.pics.domain.repository

import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    val images: Flow<List<String>>
}
