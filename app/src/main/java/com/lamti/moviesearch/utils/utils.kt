package com.lamti.moviesearch.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.animation.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.lamti.moviesearch.R

fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

@SuppressLint("CheckResult")
fun ImageView.loadImage(context: Context, posterPath: String, corners: Boolean) {

    val requestOptions = RequestOptions()
    requestOptions.placeholder(R.color.colorPrimary)
    requestOptions.error(R.color.colorAccent)
    if ( corners ) requestOptions.transforms(CenterCrop(), RoundedCorners(24))

    Glide.with(context)
        .load(posterPath)
        .apply(requestOptions)
        .into(this)
}

fun View.scaleView(startScale: Float, endScale: Float) {
    val anim = ScaleAnimation(
        startScale, endScale,
        startScale, endScale,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f
    )
    anim.fillAfter = true // Needed to keep the result of the animation
    anim.interpolator = FastOutSlowInInterpolator()
    anim.interpolator = BounceInterpolator()
    anim.duration = 650
    this.startAnimation(anim)
}
