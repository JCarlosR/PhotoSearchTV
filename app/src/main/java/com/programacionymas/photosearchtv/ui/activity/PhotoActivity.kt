package com.programacionymas.photosearchtv.ui.activity

import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.programacionymas.photosearchtv.model.Photo
import com.programacionymas.photosearchtv.R
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : FragmentActivity() {

    private lateinit var urls: Array<String>
    private var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        urls = intent?.getStringArrayExtra(PHOTO_URLS) as Array<String>
        index = intent?.getIntExtra(PHOTO_INDEX, 0) ?: 0

        displayImage()
    }

    private fun displayImage() {
        Glide.with(this)
            .load(urls[index])
            .centerCrop()
            .into(imageView)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // we will execute actions for the DOWN event
        if (event.action == KeyEvent.ACTION_UP) {
            return true
        }

        when (event.keyCode) {
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                index += 1

                if (index == urls.size) {
                    index = 0
                }

                displayImage()
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                index -= 1

                if (index < 0) {
                    index = urls.size -1
                }

                displayImage()
            }
            KeyEvent.KEYCODE_BACK -> {
                finish();
            }
            else -> {
                Toast.makeText(baseContext, "Keycode = " + event.keyCode, Toast.LENGTH_SHORT).show()
            }
        }

        return true
    }

    companion object {
        const val SHARED_ELEMENT_NAME = "hero"
        const val PHOTO_URLS = "PhotoUrls"
        const val PHOTO_INDEX = "PhotoIndex"
    }
}
