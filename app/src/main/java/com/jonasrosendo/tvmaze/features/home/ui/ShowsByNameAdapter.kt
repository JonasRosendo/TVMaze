package com.jonasrosendo.tvmaze.features.home.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jonasrosendo.model.showsbyname.Show
import com.jonasrosendo.model.showsbyname.ShowByNameResponse
import com.jonasrosendo.shared_logic.images.checkShowImageAvailability
import com.jonasrosendo.shared_logic.images.load
import com.jonasrosendo.shared_logic.text.buildGenresText
import com.jonasrosendo.tvmaze.databinding.ItemRecentMoviesAdapterBinding

class ShowsByNameAdapter(
    private val shows: MutableList<ShowByNameResponse>,
    private val onClick: (show: Show) -> Unit
) :
    RecyclerView.Adapter<ShowsByNameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecentMoviesAdapterBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val show = shows[position]
        show.show?.let {
            holder.bind(it, onClick)
        }
    }

    override fun getItemCount() = shows.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateAll(newShows: List<ShowByNameResponse>) {
        if (shows.isNotEmpty()) shows.clear()
        shows.addAll(newShows)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemRecentMoviesAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(show: Show, onClick: (show: Show) -> Unit) {

            binding.popularShowsImageView.load(checkShowImageAvailability(show))

            binding.popularShowTitleTextView.text = show.name
            binding.popularShowGenresTextView.text = buildGenresText(show)

            binding.recentMoviesLayout.setOnClickListener { onClick(show) }
        }

    }
}