package com.jim.quickjournal.extentions

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.jim.quickjournal.R


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

fun Fragment.navigate(
    destinationID: Int,
    bundle: Bundle? = null,
    clearCurrentFragmentFromBackStack: Boolean? = null
) = with(NavHostFragment.findNavController(this)) {
    clearCurrentFragmentFromBackStack?.let { needToClear ->
        if (needToClear) popBackStack()
    }
    navigate(destinationID, bundle)
}


fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .apply {
            setAction("Ok") { this.dismiss() }.show()
        }
}

fun View?.hideSoftKeyboard() {
    val imm = this?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(this?.windowToken, 0)
}


fun MaterialButton.setShowProgress(
    showProgress: Boolean?,
    textSource: String?,
) {
    iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START // center icon
    isClickable = showProgress == false
    text = if (showProgress == true) "" else textSource

    icon = if (showProgress == true) {
        CircularProgressDrawable(context).apply {
            setStyle(CircularProgressDrawable.LARGE)
            setColorSchemeColors(ContextCompat.getColor(context, R.color.colorAccent))
            start()
        }
    } else null

    icon?.let {
        icon.callback = object : Drawable.Callback {
            override fun unscheduleDrawable(who: Drawable, what: Runnable) {}
            override fun invalidateDrawable(who: Drawable) {
                this@setShowProgress.invalidate()
            }

            override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {}
        }
    }
}