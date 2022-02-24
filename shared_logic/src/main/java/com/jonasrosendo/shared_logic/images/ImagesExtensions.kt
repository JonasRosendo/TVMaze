package com.jonasrosendo.shared_logic.images

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jonasrosendo.model.episode.EpisodeResponse
import com.jonasrosendo.model.episode.Image as EpisodeImage
import com.jonasrosendo.model.showsbyname.Image as ShowImage
import com.jonasrosendo.model.showsbyname.Show
import com.jonasrosendo.shared_logic.R

fun ImageView.load(url: String) {

    val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.tvm_header_logo)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

fun checkShowImageAvailability(show: Show): String {
    var imageUrl = ""
    val image: ShowImage? = show.image
    if (image != null) {
        val original = image.original
        val medium = image.medium

        imageUrl = when {
            original != null -> {
                original
            }
            medium != null -> {
                medium
            }
            else -> ""
        }
    }

    return imageUrl
}

fun checkEpisodeImageAvailability(episode: EpisodeResponse): String {
    var imageUrl = ""
    val image: EpisodeImage? = episode.image
    if (image != null) {
        val original = image.original
        val medium = image.medium

        imageUrl = when {
            original != null -> {
                original
            }
            medium != null -> {
                medium
            }
            else -> ""
        }
    }

    return imageUrl
}
