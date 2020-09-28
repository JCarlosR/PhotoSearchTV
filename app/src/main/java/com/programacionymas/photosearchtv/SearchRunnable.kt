package com.programacionymas.photosearchtv

import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import com.programacionymas.model.Photo
import com.programacionymas.model.PhotoList

class SearchRunnable(private val updateRowsAdapter: (List<ListRow>)->Unit): Runnable {

    var searchQuery: String = ""

    override fun run() {


        updateRowsAdapter(getResults())
    }

    private fun getResults(): List<ListRow> {
        val listRowAdapter = ArrayObjectAdapter(CardPresenter())

        return if (searchQuery.length < 3) {
            val resultsHeader = HeaderItem(0, "No search results for '$searchQuery'")

            listRowAdapter.add(Photo())

            listOf(
                ListRow(resultsHeader, listRowAdapter)
            )
        } else {
            val resultsHeader = HeaderItem(0, "Search results for '$searchQuery'")

            listRowAdapter.add(PhotoList.list[0])

            listOf(
                ListRow(resultsHeader, listRowAdapter)
            )
        }

    }
}