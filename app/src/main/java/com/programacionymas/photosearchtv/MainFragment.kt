package com.programacionymas.photosearchtv

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.programacionymas.io.MyApiAdapter
import com.programacionymas.io.response.GetPhotosResponse
import com.programacionymas.model.Photo
import com.programacionymas.model.PhotoList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

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

        loadRows()

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

    private fun loadRows() {
        val call = MyApiAdapter.getApiService().getPhotos("869d0e99855f9a170627b77ef02bc13a", galleryId = "66911286-72157647277042064")
        call.enqueue(this)


        val list = PhotoList.list

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()

        for (i in 0 until NUM_ROWS) {

            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
            for (j in 0 until NUM_COLS) {
                listRowAdapter.add(list[j % 5])
            }

            // val header = HeaderItem(i.toLong(), MovieList.MOVIE_CATEGORY[i])
            // rowsAdapter.add(ListRow(header, listRowAdapter))
            rowsAdapter.add(ListRow(listRowAdapter))
        }

        // rowsAdapter.add(getPreferencesListRow())

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
                intent.putExtra(PhotoActivity.MOVIE, item)

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

    private inner class GridItemPresenter : Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
            val view = TextView(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT)
            view.isFocusable = true
            view.isFocusableInTouchMode = true

            activity?.let {
                view.setBackgroundColor(ContextCompat.getColor(it, R.color.default_background))
            }


            view.setTextColor(Color.WHITE)
            view.gravity = Gravity.CENTER
            return Presenter.ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
            (viewHolder.view as TextView).text = item as String
        }

        override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {}
    }

    companion object {
        private const val TAG = "MainFragment"

        private const val GRID_ITEM_WIDTH = 200
        private const val GRID_ITEM_HEIGHT = 200
        private const val NUM_ROWS = 6
        private const val NUM_COLS = 15
    }

    override fun onResponse(call: Call<GetPhotosResponse>, response: Response<GetPhotosResponse>) {
        if (response.isSuccessful) {
            response.body()?.let {
                val photos = it.photos.photo
                Toast.makeText(activity, "size = ${photos.size}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFailure(call: Call<GetPhotosResponse>, t: Throwable) {

    }
}
