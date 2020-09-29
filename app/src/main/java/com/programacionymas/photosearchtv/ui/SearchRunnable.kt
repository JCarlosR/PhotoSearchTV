package com.programacionymas.photosearchtv.ui

import com.programacionymas.photosearchtv.io.response.GetPhotosResponse
import com.programacionymas.photosearchtv.ui.fragment.MainFragment
import com.programacionymas.photosearchtv.ui.manager.ResultsManager
import com.programacionymas.photosearchtv.ui.util.GetPhotosService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRunnable(
    private val resultsManager: ResultsManager
): Runnable,
    Callback<GetPhotosResponse> {

    var searchQuery: String = ""
    var page: Int = 1 // fetching page

    override fun run() {
        if (searchQuery.isEmpty()) {
            resultsManager.updateAdapterMessage("Please enter a text")
            return
        }

        resultsManager.fetching = true

        GetPhotosService(MainFragment.NUM_COLS, page)
            .enqueue(this, searchQuery)
    }

    override fun onResponse(call: Call<GetPhotosResponse>, response: Response<GetPhotosResponse>) {
        if (response.isSuccessful) {
            response.body()?.let {
                val photos = it.photos.photo
                val fetchedPage = it.photos.page

                resultsManager.processPhotos(photos, fetchedPage, searchQuery)
            }
        }

        resultsManager.fetching = false
    }

    override fun onFailure(call: Call<GetPhotosResponse>, t: Throwable) {
        resultsManager.updateAdapterMessage(t.localizedMessage ?: "")

        resultsManager.fetching = false
    }
}