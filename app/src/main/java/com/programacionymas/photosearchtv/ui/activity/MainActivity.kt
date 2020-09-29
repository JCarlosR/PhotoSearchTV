package com.programacionymas.photosearchtv.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.programacionymas.photosearchtv.R

/**
 * Loads [MainFragment].
 */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
