package com.lamti.moviesearch.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.lamti.moviesearch.R
import com.lamti.moviesearch.data.network.Api
import kotlinx.android.synthetic.main.movie_list_item.view.*

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
