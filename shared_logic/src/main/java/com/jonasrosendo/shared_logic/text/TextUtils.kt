package com.jonasrosendo.shared_logic.text

import android.os.Build
import android.text.Html
import android.text.Spanned
import com.jonasrosendo.model.showsbyname.Show

fun buildGenresText(show: Show): String {
    var genres = ""
    show.genres?.forEach { genre ->
        genres += if (genres.isEmpty()) genre else ", $genre"
    }
    return genres
}

fun showHtmlText(html: String?): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(
            html,
            Html.FROM_HTML_MODE_COMPACT
        )
    else
        Html.fromHtml(html)
}