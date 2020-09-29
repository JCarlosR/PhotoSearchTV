package com.programacionymas.photosearchtv.ui.manager

import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import com.programacionymas.photosearchtv.model.Photo
import com.programacionymas.photosearchtv.ui.fragment.MainFragment
import com.programacionymas.photosearchtv.ui.presenter.CardPresenter

class PhotosManager(
    private val mPhotoPages: HashMap<Int, List<Photo>>,
    private val rowsAdapter: ArrayObjectAdapter
) {

    var lastFetchedPage: Int = 1
    var fetching: Boolean = false

    private var hasMorePages: Boolean = true

    private fun loadRows(fetchedPage: Int) {
        val listRowAdapter = ArrayObjectAdapter(CardPresenter())

        val photos: List<Photo>? = mPhotoPages[fetchedPage]

        photos?.let {
            for (photo in it) {
                listRowAdapter.add(photo)
            }

            val header = if (fetchedPage == 1) {
                HeaderItem(0, "Trending Now On Flickr")
            } else {
                null
            }

            rowsAdapter.add(ListRow(fetchedPage.toLong(), header, listRowAdapter))
        }
    }

    fun processPhotos(photos: List<Photo>, fetchedPage: Int) {
        if (photos.isEmpty()) {
            hasMorePages = false
            return
        }

        if (photos.size < MainFragment.NUM_COLS) {
            hasMorePages = false
        }

        mPhotoPages[fetchedPage] = photos

        if (fetchedPage > lastFetchedPage)
            lastFetchedPage = fetchedPage

        loadRows(fetchedPage)
    }

    fun shouldFetch(selectedPage: Int): Boolean {
        return !fetching && selectedPage == lastFetchedPage && hasMorePages
    }

}