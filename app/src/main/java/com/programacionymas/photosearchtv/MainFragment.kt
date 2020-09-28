package com.programacionymas.photosearchtv

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
import com.programacionymas.io.MyApiAdapter
import com.programacionymas.io.response.GetPhotosResponse
import com.programacionymas.model.Photo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseSupportFragment(), Callback<GetPhotosResponse> {

    private lateinit var mBackgroundManager: BackgroundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        headersState = HEADERS_DISABLED
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)

        prepareBackgroundManager()

        setupUIElements()

        fetchPhotos()

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
            // set search icon color
            searchAffordanceColor = ContextCompat.getColor(it, R.color.search_opaque)
        }
    }

    private fun fetchPhotos() {
        val call = MyApiAdapter.getApiService().getPhotos("869d0e99855f9a170627b77ef02bc13a", galleryId = "66911286-72157647277042064")
        call.enqueue(this)
    }

    private fun loadRows(list: ArrayList<Photo>) {

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()


        val listRowAdapter = ArrayObjectAdapter(cardPresenter)
        for (j in 0 until NUM_COLS) {
            listRowAdapter.add(list[j])
        }

        // val header = HeaderItem(i.toLong(), MovieList.MOVIE_CATEGORY[i])
        // rowsAdapter.add(ListRow(header, listRowAdapter))
        rowsAdapter.add(ListRow(listRowAdapter))

        adapter = rowsAdapter
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        }

        onItemViewClickedListener = ItemViewClickedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
                itemViewHolder: Presenter.ViewHolder,
                item: Any,
                rowViewHolder: RowPresenter.ViewHolder,
                row: Row) {

            if (item is Photo) {
                Log.d(TAG, "Item: $item")
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

    companion object {
        private const val TAG = "MainFragment"
        private const val NUM_COLS = 6
    }

    override fun onResponse(call: Call<GetPhotosResponse>, response: Response<GetPhotosResponse>) {
        if (response.isSuccessful) {
            response.body()?.let {
                val photos = it.photos.photo

                loadRows(photos)
            }
        }
    }

    override fun onFailure(call: Call<GetPhotosResponse>, t: Throwable) {
        Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT).show()
    }
}
