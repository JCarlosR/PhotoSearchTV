package com.programacionymas.photosearchtv.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.programacionymas.photosearchtv.io.response.GetPhotosResponse
import com.programacionymas.photosearchtv.model.Photo
import com.programacionymas.photosearchtv.R
import com.programacionymas.photosearchtv.ui.activity.SearchActivity
import com.programacionymas.photosearchtv.ui.listener.ItemViewClickedListener
import com.programacionymas.photosearchtv.ui.util.GetPhotosService
import com.programacionymas.photosearchtv.ui.manager.ColorBackgroundManager
import com.programacionymas.photosearchtv.ui.manager.PhotosManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : BrowseSupportFragment(), Callback<GetPhotosResponse> {

    private val mPhotoPages: HashMap<Int, List<Photo>> = HashMap()
    private val mPhotoUrls: HashMap<Int, List<String>> = HashMap()

    private val rowsAdapter by lazy {
        ArrayObjectAdapter(ListRowPresenter())
    }

    private val photosManager by lazy {
        PhotosManager(mPhotoPages, mPhotoUrls, rowsAdapter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        headersState = HEADERS_DISABLED
        fetchPhotos(1)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            ColorBackgroundManager(it).set(Color.BLACK)
        }

        setupUIElements()

        setupEventListeners()

        // To test Crashlytics
        // throw RuntimeException("Test Crash")
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)

        isHeadersTransitionOnBackEnabled = true

        activity?.let {
            // search button color
            searchAffordanceColor = ContextCompat.getColor(it, R.color.search_opaque)
        }

        adapter = rowsAdapter
    }

    private fun fetchPhotos(page: Int) {
        Log.d("MainFragment", "Fetching page $page")

        photosManager.fetching = true
        GetPhotosService(NUM_COLS, page).enqueue(this)
    }



    private fun setupEventListeners() {
        setOnSearchClickedListener {
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        }

        activity?.let {
            onItemViewClickedListener = ItemViewClickedListener(it, mPhotoUrls)
        }

        onItemViewSelectedListener = ItemViewSelectedListener()
    }


    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {

        override fun onItemSelected(itemViewHolder: Presenter.ViewHolder?, item: Any?,
                                    rowViewHolder: RowPresenter.ViewHolder, row: Row) {

            val page = row.id

            Log.d("MainFragment", "row.id (page) = $page")

            if (photosManager.shouldFetch(page.toInt())) {
                fetchPhotos(photosManager.lastFetchedPage + 1)
            }
        }
    }

    companion object {
        private const val TAG = "MainFragment"
        const val NUM_COLS = 6
    }

    override fun onResponse(call: Call<GetPhotosResponse>, response: Response<GetPhotosResponse>) {
        if (response.isSuccessful) {
            response.body()?.let {
                val photos = it.photos.photo
                val fetchedPage = it.photos.page

                photosManager.processPhotos(photos, fetchedPage)
            }
        }

        photosManager.fetching = false
    }

    override fun onFailure(call: Call<GetPhotosResponse>, t: Throwable) {
        Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT).show()

        photosManager.fetching = false
    }
}
