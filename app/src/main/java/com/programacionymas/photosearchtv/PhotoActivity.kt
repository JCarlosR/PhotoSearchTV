package com.programacionymas.photosearchtv

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/** Loads [PhotoFragment]. */
class PhotoActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_details)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, PhotoFragment())
                    .commit()
        }
    }

    companion object {
        const val SHARED_ELEMENT_NAME = "hero"
        const val MOVIE = "Movie"
    }
}
