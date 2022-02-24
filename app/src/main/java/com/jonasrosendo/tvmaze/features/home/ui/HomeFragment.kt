package com.jonasrosendo.tvmaze.features.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.jonasrosendo.model.shows.ShowResponse
import com.jonasrosendo.model.showsbyname.Show
import com.jonasrosendo.model.showsbyname.ShowByNameResponse
import com.jonasrosendo.shared_logic.exception.Failure
import com.jonasrosendo.shared_logic.view.gone
import com.jonasrosendo.shared_logic.view.invisible
import com.jonasrosendo.shared_logic.view.observeState
import com.jonasrosendo.shared_logic.view.visible
import com.jonasrosendo.tvmaze.R
import com.jonasrosendo.tvmaze.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val pagedShowsAdapter by lazy {
        PagedShowsAdapter { show: ShowResponse ->
            navigateToDetails(show.id)
        }
    }

    private val showsByNameAdapter by lazy {
        ShowsByNameAdapter(arrayListOf()) { show: Show ->
            navigateToDetails(show.id)
        }
    }

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.allShowsRecyclerView.run {
            setItemViewCacheSize(20)
            setHasFixedSize(true)
        }

        binding.allShowsRecyclerView.adapter = pagedShowsAdapter

        observeState(viewModel.bindState(), ::renderState)

        binding.searchButton.setOnClickListener {
            searchShowsByName()
        }
    }

    private fun renderState(state: HomeViewState) {
        when (state) {
            HomeViewState.Empty -> renderEmptyState()
            HomeViewState.Loading -> renderLoadingState()
            is HomeViewState.Error -> renderError(state.failure)
            is HomeViewState.ShowRetrievedShowsByPage -> renderRetrievedShowsByPage(state.showResponse)
            is HomeViewState.ShowsByNameSuccess -> renderShowsByNameSuccess(state.showResponse)
            is HomeViewState.ShowsByNameError -> renderShowsByNameError(state.failure)
        }
    }

    private fun renderShowsByNameError(failure: Failure) {
        genericError()
    }

    private fun renderShowsByNameSuccess(shows: List<ShowByNameResponse>) {
        showProgressBar(false)
        if (shows.isEmpty()) {
            showCustomMessage(
                "We couldn't find any show \nwith this name",
                showTryAgainButton = true
            ) {
                searchShowsByName()
            }

            binding.run {
                homeSearchEditText.visible()
                searchButton.visible()
            }

        } else {
            binding.run {
                allShowsRecyclerView.adapter = showsByNameAdapter
                showsByNameAdapter.updateAll(shows)

                backToAllShowsButton.visible()
                backToAllShowsButton.setOnClickListener {
                    homeSearchEditText.setText("")
                    searchShowsPaged()
                }
            }

        }
    }

    private fun renderRetrievedShowsByPage(shows: PagingData<ShowResponse>) {
        binding.allShowsRecyclerView.adapter = pagedShowsAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            pagedShowsAdapter.loadStateFlow.collectLatest { loadStates ->

                when (loadStates.refresh) {
                    is LoadState.NotLoading -> showProgressBar(false)
                    LoadState.Loading -> showProgressBar()
                    is LoadState.Error -> {
                        genericError()
                    }
                }
            }
        }

        lifecycleScope.launch {
            pagedShowsAdapter.submitData(shows)
        }

        showProgressBar(false)
    }

    private fun renderError(failure: Failure) {
        genericError()
    }

    private fun renderEmptyState() {
        searchShowsPaged()
    }

    private fun renderLoadingState() {
        showProgressBar()
    }

    private fun showProgressBar(show: Boolean = true) {
        if (show) {
            binding.run {
                homeProgressBar.visible()

                popularSectionTextView.invisible()
                allShowsRecyclerView.invisible()

                tryAgainButton.gone()
                customMessageTextView.gone()
                searchButton.gone()
                backToAllShowsButton.gone()
                homeSearchEditText.gone()
                titleTextView.gone()
            }
        } else {
            binding.run {
                homeProgressBar.gone()

                homeSearchEditText.visible()
                searchButton.visible()
                titleTextView.visible()
                popularSectionTextView.visible()
                allShowsRecyclerView.visible()
            }
        }
    }

    private fun showCustomMessage(
        message: String,
        showTryAgainButton: Boolean = false,
        tryAgainAction: () -> Unit = {}
    ) {
        binding.run {
            allShowsRecyclerView.gone()
            popularSectionTextView.gone()
            homeProgressBar.gone()
            searchButton.gone()
            homeSearchEditText.gone()
            titleTextView.gone()
            backToAllShowsButton.gone()

            customMessageTextView.visible()
            customMessageTextView.text = message

            if (showTryAgainButton) {
                tryAgainButton.visible()
                tryAgainButton.setOnClickListener { tryAgainAction() }
            } else {
                tryAgainButton.gone()
            }
        }
    }

    private fun searchShowsByName() {
        val showName = binding.homeSearchEditText.text.toString().trim()
        viewModel.interact(HomeViewInteraction.SearchShowByName(showName))
    }

    private fun searchShowsPaged() {
        viewModel.interact(HomeViewInteraction.GetShows)
    }

    private fun genericError() {
        showProgressBar(false)
        showCustomMessage(
            "It looks like we had some \nproblems to find shows! :(",
            showTryAgainButton = true
        ) {
            searchShowsPaged()
        }
    }

    private fun navigateToDetails(showId: Int) {
        findNavController()
            .navigate(
                R.id.action_homeFragment_to_showDetailsFragment,
                bundleOf(SHOW_ID to showId)
            )
    }

    companion object {
        const val SHOW_ID = "SHOW_ID"
    }
}
