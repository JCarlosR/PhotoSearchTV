package com.programacionymas.photosearchtv

import android.os.Bundle
import android.widget.Toast
import androidx.leanback.app.BrandedSupportFragment
import com.programacionymas.model.Photo

/** Handles video playback with media controls. */
class PhotoFragment : BrandedSupportFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val (_, title, description, _, _, videoUrl) =
                activity?.intent?.getSerializableExtra(PhotoActivity.MOVIE) as Photo

        // mSelectedMovie = activity?.intent?.getSerializableExtra(DetailsActivity.MOVIE) as Movie

        Toast.makeText(activity, title, Toast.LENGTH_SHORT).show()
    }

}
