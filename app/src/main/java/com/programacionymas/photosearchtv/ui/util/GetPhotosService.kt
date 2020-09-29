package com.programacionymas.photosearchtv.ui.util

import com.programacionymas.photosearchtv.io.MyApiAdapter
import com.programacionymas.photosearchtv.io.response.GetPhotosResponse
import retrofit2.Callback

class GetPhotosService(private val perPage: Int, private val page: Int) {

    fun enqueue(callback: Callback<GetPhotosResponse>, text: String? = null) {

        val call = if (text == null)
            MyApiAdapter.getApiService().getPhotos(
                API_KEY, galleryId = GALLERY_ID,
                page = page, perPage = perPage
            )
        else
            MyApiAdapter.getApiService().getPhotos(
                API_KEY, method = METHOD_SEARCH, text = text,
                page = page, perPage = perPage
            )

        call.enqueue(callback)
    }

    companion object {
        private const val API_KEY = "869d0e99855f9a170627b77ef02bc13a"
        private const val GALLERY_ID = "66911286-72157647277042064"
        private const val METHOD_SEARCH = "flickr.photos.search"
    }
}