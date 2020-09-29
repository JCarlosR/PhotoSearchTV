package com.programacionymas.photosearchtv.ui.listener

import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import androidx.leanback.widget.*
import com.programacionymas.photosearchtv.model.Photo
import com.programacionymas.photosearchtv.ui.activity.PhotoActivity

class ItemViewClickedListener(
    private val activity: FragmentActivity,
    private val urls: HashMap<Int, List<String>>
) : OnItemViewClickedListener {
    override fun onItemClicked(
        itemViewHolder: Presenter.ViewHolder,
        item: Any,
        rowViewHolder: RowPresenter.ViewHolder,
        row: Row
    ) {
        if (item !is Photo)
            return

        val page = row.id.toInt()

        urls[page]?.let { pageUrls ->
            val position = pageUrls.indexOf(item.getUrlLarge())

            val intent = Intent(activity, PhotoActivity::class.java)
            intent.putExtra(PhotoActivity.PHOTO_URLS, pageUrls.toTypedArray())
            intent.putExtra(PhotoActivity.PHOTO_INDEX, position)

            start(activity, (itemViewHolder.view as ImageCardView), intent)
        }

    }

    private fun start(activity: FragmentActivity, imageCardView: ImageCardView, intent: Intent) {
        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity,
            imageCardView.mainImageView,
            PhotoActivity.SHARED_ELEMENT_NAME
        ).toBundle()

        activity.startActivity(intent, bundle)
    }
}