package com.longkd.cinema.moviedetail.ui.tvshow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.longkd.cinema.ImagesConfigData
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.core.fragment.ToolbarConfiguration
import com.longkd.cinema.databinding.FragmentTvShowDetailBinding
import com.longkd.cinema.moviedetail.domain.model.MovieDetail
import com.longkd.cinema.moviedetail.domain.model.TvShowDetail
import com.longkd.cinema.moviedetail.ui.CastCrewAdapter
import com.longkd.cinema.profile.settings.SeasonListItem
import com.longkd.cinema.utils.lifecycle.observe
import com.longkd.cinema.utils.showTextToast
import com.longkd.cinema.utils.viewbinding.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class TvShowDetailFragment : BaseFragment(R.layout.fragment_tv_show_detail) {
    private val toolbarConfiguration = ToolbarConfiguration(
        startIconResId = R.drawable.ic_arrow_back,
        startIconClick = ::navBack,
        endIconResId = R.drawable.ic_wishlist,
        endIconClick = ::addOrRemoveToWishList
    )
    override val fragmentConfiguration = FragmentConfiguration()

    private val binding by viewBinding(FragmentTvShowDetailBinding::bind)

    private val viewModel by viewModels<TvShowDetailViewModel>()

    private var isWishListed = false

    private val castCrewAdapter = CastCrewAdapter(){
        nav(TvShowDetailFragmentDirections.actionTvShowDetailFragmentToPersonFragment(it))
    }

    private val episodesAdapter = EpisodesAdapter()

    private var seasonsDialog: SeasonDialog? = null

    private var seasonsList: MutableList<SeasonListItem> = mutableListOf()

    private val seasonDialogListener = SeasonDialog.OnDifferentSeasonSelect {
        seasonsList.forEach { item ->
            item.isSelected = false
        }
        seasonsList.find { s ->
            s.seasonId == it.seasonId
        }.also { selectedItem ->
            selectedItem?.isSelected = true
        }
        binding.seasonsButton.text = it.seasonTitle
        viewModel.getSeasonEpisodes(it.seasonId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIWithObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTvShowDetails()
        viewModel.getShowCastCrew()
        viewModel.getSeasonEpisodes(1)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    private fun initUIWithObservers() {
        activity?.window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        binding.showMoreButton.setOnClickListener {
            binding.storyLineTextView.maxLines = Int.MAX_VALUE
            it.visibility = View.GONE
        }
        binding.customToolbar.configure(toolbarConfiguration)
        hideBottomNavbar()
        with(binding) {
            castCrewRecyclerView.adapter = castCrewAdapter
            episodesRecyclerView.adapter = episodesAdapter
        }

        viewLifecycleOwner.observe {
            viewModel.showDetailState.collectLatest { showDetailState ->
                showDetailState.tvShowDetail?.let { tvShowDetail ->
                    if (seasonsList.isEmpty()) {
                        for (i in 1..tvShowDetail.number_of_seasons) {
                            val isSelected = i == 1
                            val seasonListItem =
                                SeasonListItem(
                                    seasonId = i,
                                    seasonTitle = getString(R.string.season, i),
                                    isSelected = isSelected
                                )
                            seasonsList.add(seasonListItem)
                        }
                        initSeasonsDialog()
                    }
                    with(binding) {
                        seasonsButton.text = seasonsList.firstOrNull()?.seasonTitle
                        customToolbar.setTitle(tvShowDetail.name)
                        val posterUrl =
                            ImagesConfigData.secure_base_url + ImagesConfigData.poster_sizes?.get(3) + tvShowDetail.poster_path
                        Glide.with(requireContext())
                            .load(posterUrl)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .centerCrop().run {
                                into(backgroundImageView)
                                into(posterImageView)
                            }
                        shareButton.setOnClickListener {
                            val sendIntent = Intent()
                            sendIntent.action = Intent.ACTION_SEND
                            sendIntent.putExtra(
                                Intent.EXTRA_TEXT,
                                tvShowDetail.name
                            )
                            sendIntent.type = "text/plain"
                            startActivity(sendIntent)
                        }
                        playButton.setOnClickListener {
                            navToShowTrailerFragment(tvShowDetail = tvShowDetail)
                        }
                        yearTextView.text = tvShowDetail.first_air_date
                        ratingTextView.text = tvShowDetail.vote_average.toString()
                        genreTextView.text = tvShowDetail.genres
                        runtimeTextView.text = getString(R.string.minutes, tvShowDetail.episode_run_time.toString())
                        storyLineTextView.text = tvShowDetail.overview

                        seasonsButton.setOnClickListener {
                            seasonsDialog?.startDialog()
                        }
                    }
                }
                if (showDetailState.isLoading) {
                    showProgressDialog()
                } else {
                    hideProgressDialog()
                }
            }
        }
        viewLifecycleOwner.observe {
            viewModel.movieCastCrewList.collectLatest {
                castCrewAdapter.submitList(it)
            }
        }
        viewLifecycleOwner.observe {
            viewModel.episodesState.collectLatest {
                episodesAdapter.submitList(it)
            }
        }
        viewLifecycleOwner.observe {
            viewModel.wishListedState.collectLatest {
                isWishListed = it
                if (isWishListed) {
                    binding.customToolbar.changeEndButtonDrawable(R.drawable.ic_wishlist)
                } else {
                    binding.customToolbar.changeEndButtonDrawable(R.drawable.ic_wishlist_empty)
                }
            }
        }
    }

    private fun addOrRemoveToWishList() {
        if (isWishListed.not()) {
            viewModel.insertShowToDatabase()
            context?.showTextToast(getString(R.string.added_to_wishlist))
        } else {
            viewModel.removeShowFromDatabase()
            context?.showTextToast(getString(R.string.removed_from_wishlist))
        }
    }

    private fun initSeasonsDialog() {
        seasonsDialog =
            SeasonDialog(
                activity = requireActivity(),
                listener = seasonDialogListener,
                seasonList = seasonsList.toList()
            )
    }

    private fun navToShowTrailerFragment(tvShowDetail: TvShowDetail) {
        val movieDetail = MovieDetail(
            backdrop_path = tvShowDetail.backdrop_path,
            genre = tvShowDetail.genres,
            id = tvShowDetail.id,
            original_title = "",
            overview = tvShowDetail.overview,
            poster_path = null,
            release_date = tvShowDetail.first_air_date,
            runtime = 0,
            title = tvShowDetail.name,
            video = false,
            vote_average = 0.0
        )
        nav(
            TvShowDetailFragmentDirections.actionTvShowDetailFragmentToMovieTrailerFragment(
                movieDetail = movieDetail,
                isMovie = false
            )
        )
    }
}
