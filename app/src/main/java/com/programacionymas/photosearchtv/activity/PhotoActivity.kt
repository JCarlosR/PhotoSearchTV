package com.programacionymas.photosearchtv.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.programacionymas.model.Photo
import com.programacionymas.photosearchtv.R
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        val photo = intent?.getSerializableExtra(PHOTO_PARAM) as Photo

        Glide.with(this)
            .load(photo.getUrlLarge())
            .centerCrop()
            .into(imageView)
    }

    companion object {
        const val SHARED_ELEMENT_NAME = "hero"
        const val PHOTO_PARAM = "Photo"
    }
}
