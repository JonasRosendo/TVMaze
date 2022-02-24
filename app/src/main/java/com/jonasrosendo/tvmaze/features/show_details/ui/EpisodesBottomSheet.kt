package com.jonasrosendo.tvmaze.features.show_details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jonasrosendo.model.episode.EpisodeResponse
import com.jonasrosendo.tvmaze.databinding.BottomSheetEpisodesBinding

class EpisodesBottomSheet(private val episodes: List<EpisodeResponse>) :
    BottomSheetDialogFragment() {

    private lateinit var binding : BottomSheetEpisodesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetEpisodesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.episodesBottomSheetRecyclerView.adapter =
            EpisodesBottomSheetAdapter(episodes = episodes)
    }

}