package com.jonasrosendo.tvmaze.features.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jonasrosendo.model.shows.Image
import com.jonasrosendo.model.shows.ShowResponse
import com.jonasrosendo.model.showsbyname.Show
import com.jonasrosendo.shared_logic.images.load
import com.jonasrosendo.tvmaze.R
import com.jonasrosendo.tvmaze.databinding.ItemRecentMoviesAdapterBinding

class PagedShowsAdapter(private val onClick: (show: ShowResponse) -> Unit) :
    PagingDataAdapter<ShowResponse, PagedShowsAdapter.ViewHolder>(SHOW_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecentMoviesAdapterBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onClick)
        }
    }

    class ViewHolder(private val binding: ItemRecentMoviesAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(show: ShowResponse, onClick: (show: ShowResponse) -> Unit) {
            var imageUrl = ""
            val image: Image? = show.image
            if (image != null) {
                imageUrl = when {
                    image.original.isNotEmpty() -> {
                        image.original
                    }
                    image.medium.isNotEmpty() -> {
                        image.medium
                    }
                    else -> {
                        ""
                    }
                }
            }

            if (imageUrl.isNotEmpty()) {
                binding.popularShowsImageView.load(imageUrl)
            } else {
                binding.popularShowsImageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                binding.popularShowsImageView.setImageResource(R.drawable.tvm_header_logo)
            }

            binding.popularShowTitleTextView.text = show.name
            binding.popularShowGenresTextView.text = buildGenresText(show)

            binding.recentMoviesLayout.setOnClickListener {
                onClick(show)
            }
        }

        private fun buildGenresText(show: ShowResponse): String {
            var genres = ""
            show.genres?.forEach { genre ->
                genres += if (genres.isEmpty()) genre else ", $genre"
            }
            return genres
        }
    }

    private companion object {
        val SHOW_COMPARATOR = object : DiffUtil.ItemCallback<ShowResponse>() {
            override fun areItemsTheSame(
                oldItem: ShowResponse,
                newItem: ShowResponse
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ShowResponse,
                newItem: ShowResponse
            ) = oldItem.id == newItem.id
        }
    }
}