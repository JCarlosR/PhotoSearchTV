package com.programacionymas.photosearchtv.activity

import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
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


    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // we will execute actions for the DOWN event
        if (event.action == KeyEvent.ACTION_UP) {
            return true
        }

        if (event.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            Toast.makeText(baseContext, "Right", Toast.LENGTH_SHORT).show()
        } else if (event.keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            Toast.makeText(baseContext, "Left", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(baseContext, "Keycode = " + event.keyCode, Toast.LENGTH_SHORT).show()
        }

        return true
    }

    companion object {
        const val SHARED_ELEMENT_NAME = "hero"
        const val PHOTO_PARAM = "Photo"
    }
}
