package com.programacionymas.photosearchtv.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.leanback.app.SearchSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import com.programacionymas.photosearchtv.ui.SearchRunnable
import com.programacionymas.photosearchtv.ui.listener.ItemViewClickedListener
import com.programacionymas.photosearchtv.ui.manager.ColorBackgroundManager


class SearchFragment : SearchSupportFragment(), SearchSupportFragment.SearchResultProvider {
    private val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

    private val handler = Handler(Looper.getMainLooper())
    private val delayedLoad = SearchRunnable(::updateRowsAdapter)

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
            setOnItemViewClickedListener(ItemViewClickedListener(it))
        }

        // onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private fun updateRowsAdapter(listRows: List<ListRow>) {
        listRows.forEach {
            rowsAdapter.add(it)
        }
    }

    override fun getResultsAdapter() = rowsAdapter


    override fun onQueryTextChange(newQuery: String): Boolean {
        runQuery(newQuery)
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        runQuery(query)
        return true
    }

    private fun runQuery(query: String) {
        rowsAdapter.clear()

        if (!TextUtils.isEmpty(query)) {
            delayedLoad.searchQuery = query
            handler.removeCallbacks(delayedLoad)
            handler.postDelayed(delayedLoad, SEARCH_DELAY_MS)
        }
    }

    companion object {
        private const val SEARCH_DELAY_MS: Long = 300
    }
}