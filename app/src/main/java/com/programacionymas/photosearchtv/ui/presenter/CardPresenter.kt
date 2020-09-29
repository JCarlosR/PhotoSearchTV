package com.programacionymas.photosearchtv.ui.presenter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.BaseCardView
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.programacionymas.photosearchtv.model.Photo
import com.programacionymas.photosearchtv.R
import kotlin.properties.Delegates

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class CardPresenter : Presenter() {
    private var mDefaultCardImage: Drawable? = null

    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        Log.d(TAG, "onCreateViewHolder")

        sDefaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        sSelectedBackgroundColor = ContextCompat.getColor(parent.context,
            R.color.selected_background
        )

        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.drawable.movie)

        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val photo = item as Photo
        val cardView = viewHolder.view as ImageCardView

        Log.d(TAG, "onBindViewHolder")

        cardView.cardType = BaseCardView.CARD_TYPE_INFO_OVER
        cardView.titleText = photo.title
        cardView.contentText = "${photo.author} / ${photo.getFormattedDate()}"
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)

        Glide.with(viewHolder.view.context)
            .load(photo.getUrlSmall())
            .centerCrop()
            .error(mDefaultCardImage)
            .into(cardView.mainImageView)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")

        val cardView = viewHolder.view as ImageCardView
        // Remove references to images so that the garbage collector can free up memory

        cardView.badgeImage = null
        cardView.mainImage = null
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        // Both background colors should be set because the background is temporarily visible
        // during animations.

        val color = if (selected)
            sSelectedBackgroundColor
        else
            sDefaultBackgroundColor

        view.setInfoAreaBackgroundColor(color)
    }

    companion object {
        private const val TAG = "CardPresenter"

        private const val CARD_WIDTH = 313
        private const val CARD_HEIGHT = 176
    }
}
