package com.programacionymas.photosearchtv.io

import com.programacionymas.photosearchtv.io.response.GetPhotosResponse
import retrofit2.Call
import retrofit2.http.*


interface MyApiService {

    @GET("rest")
    fun getPhotos(
        @Query("api_key") apiKey: String,
        @Query("method") method: String = "flickr.galleries.getPhotos",
        @Query("nojsoncallback") noJsonCallback: Int = 1,
        @Query("gallery_id") galleryId: String,
        @Query("format") format: String = "json",
        @Query("per_page") perPage: Int = 6,
        @Query("page") page: Int = 1,
        @Query("extras") extras: String = "title, date_upload, owner_name", // title, date_upload, owner_name, tags, views
    ): Call<GetPhotosResponse>

}