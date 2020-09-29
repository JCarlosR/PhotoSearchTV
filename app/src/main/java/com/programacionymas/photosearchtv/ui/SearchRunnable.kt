package com.programacionymas.photosearchtv.ui

import android.widget.Toast
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import com.programacionymas.photosearchtv.io.MyApiAdapter
import com.programacionymas.photosearchtv.io.response.GetPhotosResponse
import com.programacionymas.photosearchtv.model.Photo
import com.programacionymas.photosearchtv.ui.fragment.MainFragment
import com.programacionymas.photosearchtv.ui.presenter.CardPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRunnable(private val updateRowsAdapter: (List<ListRow>)->Unit): Runnable,
    Callback<GetPhotosResponse> {

    var searchQuery: String = ""

    override fun run() {
        val call = MyApiAdapter.getApiService().getPhotos("869d0e99855f9a170627b77ef02bc13a", "flickr.photos.search", text = searchQuery)
        call.enqueue(this)
    }

    private fun getResults(photos: List<Photo>): List<ListRow> {
        val listRowAdapter = ArrayObjectAdapter(CardPresenter())

        val resultsHeader = HeaderItem(0, "Search results for '$searchQuery'")

        for (photo in photos)
            listRowAdapter.add(photo)

        return listOf(
            ListRow(resultsHeader, listRowAdapter)
        )
    }

    private fun getErrorMessageResults(errorMessage: String): List<ListRow> {
        val resultsHeader = HeaderItem(0, errorMessage)

        val listRowAdapter = ArrayObjectAdapter(CardPresenter())

        return listOf(
            ListRow(resultsHeader, listRowAdapter)
        )
    }

    override fun onResponse(call: Call<GetPhotosResponse>, response: Response<GetPhotosResponse>) {
        if (response.isSuccessful) {
            response.body()?.let {
                val photos = it.photos.photo
                val fetchedPage = it.photos.page

                if (photos.isEmpty()) {
                    updateRowsAdapter(getErrorMessageResults("No search results for '$searchQuery'"))
                    // hasMorePages = false
                    return
                }

                /*
                if (photos.size < MainFragment.NUM_COLS) {
                    hasMorePages = false
                }*/

                // mPhotoPages[fetchedPage] = photos

                /*
                if (fetchedPage > lastFetchedPage)
                    lastFetchedPage = fetchedPage

                loadRows(fetchedPage)
                */
                updateRowsAdapter(getResults(photos))
            }
        }
    }

    override fun onFailure(call: Call<GetPhotosResponse>, t: Throwable) {
        updateRowsAdapter(getErrorMessageResults(t.localizedMessage ?: ""))
    }
}