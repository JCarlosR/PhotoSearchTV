package com.programacionymas.photosearchtv.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.leanback.app.SearchSupportFragment
import androidx.leanback.widget.*
import com.programacionymas.photosearchtv.model.Photo
import com.programacionymas.photosearchtv.ui.SearchRunnable
import com.programacionymas.photosearchtv.ui.listener.ItemViewClickedListener
import com.programacionymas.photosearchtv.ui.manager.ColorBackgroundManager
import com.programacionymas.photosearchtv.ui.manager.ResultsManager


class SearchFragment : SearchSupportFragment(), SearchSupportFragment.SearchResultProvider {

    private val mPhotoPages: HashMap<Int, List<Photo>> = HashMap()
    private val mPhotoUrls: HashMap<Int, List<String>> = HashMap()

    private val rowsAdapter by lazy {
        ArrayObjectAdapter(ListRowPresenter())
    }

    private val resultsManager by lazy {
        ResultsManager(mPhotoPages, mPhotoUrls, rowsAdapter)
    }

    private val handler = Handler(Looper.getMainLooper())
    private val delayedLoad = SearchRunnable(resultsManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSearchResultProvider(this)

        setupEventListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            ColorBackgroundManager(it).set(Color.BLACK)
        }
    }


    private fun setupEventListeners() {
        activity?.let {
            setOnItemViewClickedListener(ItemViewClickedListener(it, mPhotoUrls))
        }

        setOnItemViewSelectedListener(ItemViewSelectedListener())
    }

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {

        override fun onItemSelected(itemViewHolder: Presenter.ViewHolder?, item: Any?,
                                    rowViewHolder: RowPresenter.ViewHolder, row: Row
        ) {

            val page = row.id

            Log.d("SearchFragment", "row.id (page) = $page")

            if (resultsManager.shouldFetch(page.toInt())) {
                runQuery(page = resultsManager.lastFetchedPage + 1)
            }
        }
    }

    override fun getResultsAdapter() = rowsAdapter

    override fun onQueryTextChange(newQuery: String): Boolean {
        rowsAdapter.clear()
        runQuery(newQuery)
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        runQuery(query)
        return true
    }

    private fun runQuery(query: String? = null, page: Int = 1) {
        query?.let {
            delayedLoad.searchQuery = it
        }
        delayedLoad.page = page

        handler.removeCallbacks(delayedLoad)
        handler.postDelayed(delayedLoad, SEARCH_DELAY_MS)
    }

    companion object {
        private const val SEARCH_DELAY_MS: Long = 300
    }
}