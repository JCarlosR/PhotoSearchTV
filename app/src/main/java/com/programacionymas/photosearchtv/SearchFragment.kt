package com.programacionymas.photosearchtv

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.leanback.app.SearchSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.ObjectAdapter

class SearchFragment : SearchSupportFragment(), SearchSupportFragment.SearchResultProvider {
    private val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    private val handler = Handler(Looper.getMainLooper())
    private val delayedLoad = SearchRunnable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSearchResultProvider(this)

        // setOnItemClickedListener(getDefaultItemClickedListener())
    }

    override fun getResultsAdapter(): ObjectAdapter {
        return rowsAdapter
    }

    override fun onQueryTextChange(newQuery: String): Boolean {
        rowsAdapter.clear()
        if (!TextUtils.isEmpty(newQuery)) {
            delayedLoad.searchQuery = newQuery
            handler.removeCallbacks(delayedLoad)
            handler.postDelayed(delayedLoad, SEARCH_DELAY_MS)
        }

        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        rowsAdapter.clear()

        if (!TextUtils.isEmpty(query)) {
            delayedLoad.searchQuery = query
            handler.removeCallbacks(delayedLoad)
            handler.postDelayed(delayedLoad, SEARCH_DELAY_MS)
        }

        return true
    }

    companion object {
        private const val SEARCH_DELAY_MS: Long = 300
    }
}