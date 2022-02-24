package com.jonasrosendo.tvmaze.features.show_details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jonasrosendo.model.episode.EpisodeResponse
import com.jonasrosendo.model.showsbyname.Schedule
import com.jonasrosendo.model.showsbyname.Show
import com.jonasrosendo.shared_logic.exception.Failure
import com.jonasrosendo.shared_logic.images.checkShowImageAvailability
import com.jonasrosendo.shared_logic.images.load
import com.jonasrosendo.shared_logic.text.buildGenresText
import com.jonasrosendo.shared_logic.text.showHtmlText
import com.jonasrosendo.shared_logic.view.gone
import com.jonasrosendo.shared_logic.view.invisible
import com.jonasrosendo.shared_logic.view.observeState
import com.jonasrosendo.shared_logic.view.visible
import com.jonasrosendo.tvmaze.R
import com.jonasrosendo.tvmaze.databinding.FragmentShowDetailsBinding
import com.jonasrosendo.tvmaze.features.home.ui.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowDetailsFragment : Fragment() {

    private lateinit var binding: FragmentShowDetailsBinding

    private val viewModel by viewModels<ShowDetailsViewModel>()

    private val showId: Int by lazy { arguments?.getInt(HomeFragment.SHOW_ID) ?: 0 }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState(viewModel.bindState(), ::renderStates)
    }

    private fun renderStates(state: ShowDetailsViewState) {
        when (state) {
            ShowDetailsViewState.Empty -> renderEmptyState()
            is ShowDetailsViewState.GetShowByIdFailure -> renderGetShowByIdFailure(state.failure)
            is ShowDetailsViewState.GetShowByIdSuccess -> renderGetShowByIdSuccess(
                state.show,
                state.episodes
            )
            ShowDetailsViewState.Loading -> renderLoading()
            ShowDetailsViewState.Opened -> renderOpened()
        }
    }

    private fun renderOpened() {
        if (showId > 0) {
            viewModel.interact(ShowDetailsViewInteraction.GetShowById(showId))
        } else {
            hideViews()
            binding.showDetailsErrorTextView.text = getString(R.string.error_try_again_later)
        }
    }

    private fun renderLoading() {
        hideViews()
        binding.detailsProgressBar.visible()
    }

    private fun renderGetShowByIdSuccess(show: Show, episodes: List<EpisodeResponse>) {
        binding.run {
            showDetailsNameTextView.text = show.name

            setupSchedule(show)
            setupGenres(show)

            showDetailsPosterImageView.load(checkShowImageAvailability(show))

            setupSummary(show)

            showDetailsEpisodesButton.setOnClickListener {
                EpisodesBottomSheet(episodes).show(childFragmentManager, "")
            }

            showViews()
            detailsProgressBar.gone()
            showDetailsErrorTextView.gone()
        }
    }

    private fun FragmentShowDetailsBinding.setupSummary(show: Show) {
        showDetailsSummaryTextView.text = showHtmlText(show.summary)
    }

    private fun FragmentShowDetailsBinding.setupSchedule(show: Show) {
        show.schedule?.let { schedule ->
            showDetailsDayAndTimeOnAirTextView.visible()
            "On air ${buildShowSchedule(schedule)}".also {
                showDetailsDayAndTimeOnAirTextView.text = it
            }
        }
    }

    private fun FragmentShowDetailsBinding.setupGenres(show: Show) {
        val genres = buildGenresText(show)
        if (genres.isNotEmpty()) {
            showDetailsGenresTextView.text = genres
        } else {
            showDetailsGenresTextView.invisible()
        }
    }

    private fun buildShowSchedule(schedule: Schedule): String {
        val time = schedule.time ?: ""
        var days = ""

        schedule.days?.forEach {
            days += " $it"
        }

        val onAirSchecule = if (time.isEmpty() && days.isNotEmpty()) {
            "On$days"
        } else if (time.isNotEmpty() && days.isEmpty()) {
            "At $time"
        } else if (time.isNotEmpty() && days.isNotEmpty()) {
            "At $time On$days"
        } else {
            ""
        }

        return onAirSchecule
    }

    private fun renderGetShowByIdFailure(failure: Failure) {
        hideViews()
        binding.showDetailsErrorTextView.visible()
        binding.showDetailsErrorTextView.text = getString(R.string.error_try_again_later)
    }

    private fun renderEmptyState() {
        hideViews()
        binding.showDetailsErrorTextView.visible()
        binding.showDetailsErrorTextView.text = getString(R.string.empty_show)
    }

    private fun hideViews() {
        binding.run {
            showDetailsPosterImageView.gone()
            showDetailsErrorTextView.gone()
            detailsProgressBar.gone()
            showDetailsSummaryTextView.gone()
            showDetailsNameTextView.gone()
            showDetailsEpisodesButton.gone()
            showDetailsGenresTextView.gone()
        }
    }

    private fun showViews() {
        binding.run {
            showDetailsPosterImageView.visible()
            showDetailsSummaryTextView.visible()
            showDetailsNameTextView.visible()
            showDetailsEpisodesButton.visible()
            showDetailsGenresTextView.visible()
            showDetailsErrorTextView.visible()
            detailsProgressBar.visible()
        }
    }
}
