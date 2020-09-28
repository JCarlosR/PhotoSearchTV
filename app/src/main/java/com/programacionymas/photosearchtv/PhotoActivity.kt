package com.programacionymas.photosearchtv

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/** Loads [PhotoFragment]. */
class PhotoActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, PhotoFragment())
                    .commit()
        }
    }
}
