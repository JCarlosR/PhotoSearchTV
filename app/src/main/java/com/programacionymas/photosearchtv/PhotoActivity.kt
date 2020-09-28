package com.programacionymas.photosearchtv

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.programacionymas.model.Photo
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        val (_, title, author, imageUrl) =
            intent?.getSerializableExtra(PhotoActivity.MOVIE) as Photo

        // mSelectedMovie = activity?.intent?.getSerializableExtra(DetailsActivity.MOVIE) as Movie

        Toast.makeText(this, imageUrl, Toast.LENGTH_SHORT).show()

        Glide.with(this)
            .load(imageUrl)
            .centerCrop()
            .into(imageView)
    }

    companion object {
        const val SHARED_ELEMENT_NAME = "hero"
        const val MOVIE = "Movie"
    }
}
