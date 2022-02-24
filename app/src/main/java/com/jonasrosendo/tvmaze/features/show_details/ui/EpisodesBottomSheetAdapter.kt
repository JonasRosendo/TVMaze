package com.jonasrosendo.tvmaze.features.show_details.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jonasrosendo.model.episode.EpisodeResponse
import com.jonasrosendo.shared_logic.images.checkEpisodeImageAvailability
import com.jonasrosendo.shared_logic.images.load
import com.jonasrosendo.shared_logic.text.showHtmlText
import com.jonasrosendo.shared_logic.view.gone
import com.jonasrosendo.shared_logic.view.visible
import com.jonasrosendo.tvmaze.databinding.ItemBottomSheetEpisodesBinding

class EpisodesBottomSheetAdapter(private val episodes: List<EpisodeResponse>) :
    RecyclerView.Adapter<EpisodesBottomSheetAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBottomSheetEpisodesBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(episodes[position])
    }

    override fun getItemCount() = episodes.size

    class ViewHolder(private val binding: ItemBottomSheetEpisodesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: EpisodeResponse) {

            binding.run {
                episodeNameTextView.text = episode.name
                "Episode: ${episode.number.toString()}".also { episodeNumberTextView.text = it }
                "Season: ${episode.season.toString()}".also { episodeSeasonTextView.text = it }
                episodeImageView.load(checkEpisodeImageAvailability(episode))
                episodeSummaryTextView.text = showHtmlText(episode.summary)

                binding.root.setOnClickListener {
                    if (episodeSummaryTextView.isVisible) {
                        episodeSummaryTextView.gone()
                    } else {
                        episodeSummaryTextView.visible()
                    }
                }
            }
        }
    }
}
