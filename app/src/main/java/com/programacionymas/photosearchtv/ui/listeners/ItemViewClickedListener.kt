package com.programacionymas.photosearchtv.ui.listeners

import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import androidx.leanback.widget.*
import com.programacionymas.photosearchtv.model.Photo
import com.programacionymas.photosearchtv.ui.activity.PhotoActivity

class ItemViewClickedListener(private val activity: FragmentActivity) : OnItemViewClickedListener {
    override fun onItemClicked(
        itemViewHolder: Presenter.ViewHolder,
        item: Any,
        rowViewHolder: RowPresenter.ViewHolder,
        row: Row
    ) {

        if (item is Photo) {

            val intent = Intent(activity, PhotoActivity::class.java)
            intent.putExtra(PhotoActivity.PHOTO_PARAM, item)

            activity.let {
                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    it,
                    (itemViewHolder.view as ImageCardView).mainImageView,
                    PhotoActivity.SHARED_ELEMENT_NAME)
                    .toBundle()

                it.startActivity(intent, bundle)
            }

        }
    }
}