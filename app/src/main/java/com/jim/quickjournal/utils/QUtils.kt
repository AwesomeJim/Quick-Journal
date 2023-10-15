package com.jim.quickjournal.utils

import android.graphics.Color
import java.util.Random


/**
 * Created by Awesome Jim on.
 * 15/10/2023
 */

/**
 *
 * @return Ca random color which is used a background by
 * day textview
 */
object QUtils {
    val randomColor: Int
        get() {
            val rnd = Random()
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }

    // Constant for date format
    const val DATE_FORMAT = "d MMM yyyy HH:mm aa"
    const val DATE_FORMAT_INIT = "EEE"

}