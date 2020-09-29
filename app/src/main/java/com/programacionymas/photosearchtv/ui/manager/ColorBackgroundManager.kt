package com.programacionymas.photosearchtv.ui.manager

import android.app.Activity
import androidx.leanback.app.BackgroundManager

class ColorBackgroundManager(private val activity: Activity) {

    fun set(color: Int) {
        val backgroundManager = BackgroundManager.getInstance(activity)
        backgroundManager.attach(activity.window)
        backgroundManager.color = color
    }

}