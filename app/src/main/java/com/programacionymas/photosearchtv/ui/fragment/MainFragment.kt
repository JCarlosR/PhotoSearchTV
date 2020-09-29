package com.programacionymas.photosearchtv.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.programacionymas.photosearchtv.io.MyApiAdapter
import com.programacionymas.photosearchtv.io.response.GetPhotosResponse
import com.programacionymas.photosearchtv.model.Photo
import com.programacionymas.photosearchtv.ui.presenter.CardPresenter
import com.programacionymas.photosearchtv.R
import com.programacionymas.photosearchtv.ui.activity.PhotoActivity
import com.programacionymas.photosearchtv.ui.activity.SearchActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseSupportFragment(), Callback<GetPhotosResponse> {

    private lateinit var mBackgroundManager: BackgroundManager

    private val mPhotoPages: HashMap<Int, List<Photo>> = HashMap()

    private val rowsAdapter by lazy {
        ArrayObjectAdapter(ListRowPresenter())
    }

    private var lastFetchedPage: Int = 1
    private var alreadyFetching: Boolean = false
    private var hasMorePages: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        fetchPhotos(1)
        headersState = HEADERS_DISABLED
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)

        prepareBackgroundManager()

        setupUIElements()

        setupEventListeners()
    }


    private fun prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(activity)
        mBackgroundManager.attach(activity?.window)
        mBackgroundManager.color = Color.BLACK
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)

        isHeadersTransitionOnBackEnabled = true

        activity?.let {
            // search button color
            searchAffordanceColor = ContextCompat.getColor(it, R.color.search_opaque)
        }
    }

    private fun fetchPhotos(page: Int) {
        Log.d("MainFragment", "Fetching page $page")
        alreadyFetching = true

        val call = MyApiAdapter.getApiService().getPhotos(
            "869d0e99855f9a170627b77ef02bc13a", galleryId = "66911286-72157647277042064",
            page = page, perPage = NUM_COLS
        )

        call.enqueue(this)
    }

    private fun loadRows(fetchedPage: Int) {
        val cardPresenter = CardPresenter()

        val listRowAdapter = ArrayObjectAdapter(cardPresenter)

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

        adapter = rowsAdapter
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        }

        onItemViewClickedListener = ItemViewClickedListener()
        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
                itemViewHolder: Presenter.ViewHolder,
                item: Any,
                rowViewHolder: RowPresenter.ViewHolder,
                row: Row
        ) {

            if (item is Photo) {
                // Log.d(TAG, "Item: $item")

                val intent = Intent(activity, PhotoActivity::class.java)
                intent.putExtra(PhotoActivity.PHOTO_PARAM, item)

                activity?.let {
                    val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        it,
                        (itemViewHolder.view as ImageCardView).mainImageView,
                        PhotoActivity.SHARED_ELEMENT_NAME)
                        .toBundle()

                    it.startActivity(intent, bundle)
                }

            } else if (item is String) {
                Toast.makeText(activity, item, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {

        override fun onItemSelected(itemViewHolder: Presenter.ViewHolder?, item: Any?,
                                    rowViewHolder: RowPresenter.ViewHolder, row: Row) {

            // Toast.makeText(activity, "row id (page) = ${row.id}", Toast.LENGTH_SHORT).show()
            val page = row.id

            Log.d("MainFragment", "row.id (page) = $page")
            Log.d("MainFragment", "lastFetchedPage = $lastFetchedPage")

            if (!alreadyFetching && page.toInt() == lastFetchedPage && hasMorePages) {
                fetchPhotos(lastFetchedPage + 1)
            }
        }
    }

    companion object {
        private const val TAG = "MainFragment"
        private const val NUM_COLS = 6
    }

    override fun onResponse(call: Call<GetPhotosResponse>, response: Response<GetPhotosResponse>) {
        if (response.isSuccessful) {
            response.body()?.let {
                val photos = it.photos.photo
                val fetchedPage = it.photos.page

                if (photos.isEmpty()) {
                    hasMorePages = false
                    return
                }

                if (photos.size < NUM_COLS) {
                    hasMorePages = false
                }

                mPhotoPages[fetchedPage] = photos

                if (fetchedPage > lastFetchedPage)
                    lastFetchedPage = fetchedPage

                loadRows(fetchedPage)
            }
        }

        alreadyFetching = false
    }

    override fun onFailure(call: Call<GetPhotosResponse>, t: Throwable) {
        Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT).show()

        alreadyFetching = false
    }
}
