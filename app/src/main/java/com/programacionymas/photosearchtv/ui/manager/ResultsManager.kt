package com.programacionymas.photosearchtv.ui.manager

import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import com.programacionymas.photosearchtv.model.Photo
import com.programacionymas.photosearchtv.ui.fragment.MainFragment
import com.programacionymas.photosearchtv.ui.presenter.CardPresenter

class ResultsManager(
    private val mPhotoPages: HashMap<Int, List<Photo>>,
    private val mPhotoUrls: HashMap<Int, List<String>>,
    private val rowsAdapter: ArrayObjectAdapter
) {

    var lastFetchedPage: Int = 1
    var fetching: Boolean = false

    private var hasMorePages: Boolean = true

    fun processPhotos(photos: List<Photo>, fetchedPage: Int, searchQuery: String) {
        if (photos.isEmpty()) {
            updateAdapterMessage("No search results for '$searchQuery'")
            hasMorePages = false
            return
        }

        if (photos.size < MainFragment.NUM_COLS) {
            hasMorePages = false
        }

        mPhotoPages[fetchedPage] = photos

        mPhotoUrls[fetchedPage] = photos.map { it.getUrlLarge() }

        if (fetchedPage > lastFetchedPage)
            lastFetchedPage = fetchedPage

        loadRows(fetchedPage, searchQuery)
    }

    private fun loadRows(fetchedPage: Int, searchQuery: String) {
        val listRowAdapter = ArrayObjectAdapter(CardPresenter())

        val photos: List<Photo>? = mPhotoPages[fetchedPage]

        photos?.let {
            for (photo in it) {
                listRowAdapter.add(photo)
            }

            val header = if (fetchedPage == 1) {
                HeaderItem(0, "Search results for '$searchQuery'")
            } else {
                null
            }

            rowsAdapter.add(ListRow(fetchedPage.toLong(), header, listRowAdapter))
        }
    }

    fun updateAdapterMessage(message: String) {
        val resultsHeader = HeaderItem(0, message)

        val listRowAdapter = ArrayObjectAdapter(CardPresenter())

        rowsAdapter.add(ListRow(resultsHeader, listRowAdapter))
    }

    fun shouldFetch(selectedPage: Int): Boolean {
        return !fetching && selectedPage == lastFetchedPage && hasMorePages
    }
}