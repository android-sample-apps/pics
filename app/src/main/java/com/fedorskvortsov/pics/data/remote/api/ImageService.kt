package com.fedorskvortsov.pics.data.remote.api

import retrofit2.Response
import retrofit2.http.GET

interface ImageService {

    @GET("list.php")
    suspend fun getImages(): Response<List<String>>

    companion object {
        const val BASE_URL = "http://dev-tasks.alef.im/task-m-001/"
    }
}
