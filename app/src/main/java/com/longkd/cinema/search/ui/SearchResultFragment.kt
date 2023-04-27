package com.longkd.cinema.search.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.databinding.FragmentSearchResultBinding
import com.longkd.cinema.ui.MovieBigCardItemAdapter
import com.longkd.cinema.utils.QUERY_SEARCH_DELAY
import com.longkd.cinema.utils.lifecycle.observe
import com.longkd.cinema.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SearchResultFragment : BaseFragment(R.layout.fragment_search_result) {
    override val fragmentConfiguration = FragmentConfiguration()
    private var isResumeFromVoice = false
    private lateinit var startForResult: ActivityResultLauncher<Intent>
    private val binding by viewBinding(FragmentSearchResultBinding::bind)

    private val viewModel by viewModels<SearchResultViewModel>()

    private val personItemAdapter = PersonItemAdapter {
        nav(SearchResultFragmentDirections.actionSearchResultFragmentToPersonFragment(it))
    }

    private var searchJob: Job? = null

    private val movieBigCardAdapterListener = MovieBigCardItemAdapter.MoviesBigCardAdapterListener { movieBigCardItem ->
        if (movieBigCardItem.mediaType != "tv") {
            nav(SearchResultFragmentDirections.actionSearchResultFragmentToMovieDetailFragment(movieBigCardItem.id))
        } else {
            nav(SearchResultFragmentDirections.actionSearchResultFragmentToTvShowDetailFragment(movieBigCardItem.id))
        }
    }

    private val movieBigCardItemAdapter = MovieBigCardItemAdapter(movieBigCardAdapterListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initResult()
        initUI()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        if (!isResumeFromVoice) searchJob?.cancel()
        else isResumeFromVoice = false
    }

    private fun initResult() {
        startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val res: ArrayList<String> =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                binding.searchEditText.setText(Objects.requireNonNull(res)[0])
                isResumeFromVoice = true
            }
        }
    }

    private fun initUI() {
        hideBottomNavbar()
        with(binding) {
            actorsRecyclerView.adapter = personItemAdapter
            moviesRecyclerView.adapter = movieBigCardItemAdapter
            btnVoice.setOnClickListener {
                val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
                sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak))
                startForResult.launch(sttIntent)
            }
            searchEditText.apply {
                doAfterTextChanged { searchQuery ->
                    searchQuery?.let {
                        searchJob?.cancel()
                        searchJob = lifecycleScope.launch {
                            delay(QUERY_SEARCH_DELAY)
                            if (searchQuery.trim().length > 1) {
                                viewModel.searchPerson(searchQuery.toString())
                                viewModel.searchMovie(searchQuery.toString())
                            }
                        }
                    }
                }
                //For submit button
                setOnEditorActionListener { textView, _, _ ->
                    if (textView.text.trim().length > 1) {
                        viewModel.searchPerson(textView.text.toString())
                        viewModel.searchMovie(textView.text.toString())
                    }
                    return@setOnEditorActionListener true
                }
            }

            lifecycleScope.launch {
                movieBigCardItemAdapter.loadStateFlow.collectLatest { loadState ->
                    val isListEmpty =
                        loadState.refresh is LoadState.NotLoading && movieBigCardItemAdapter.itemCount == 0

                    checkEmptyMovieList(isListEmpty)
                }
            }
        }
    }

    //TODO REFACTOR Here using state model etc.
    private fun initObservers() {
        with(binding) {
            viewLifecycleOwner.observe {
                viewModel.personListState.collectLatest {
                    personItemAdapter.submitList(it.personItemList)
                    if (it.personItemList.isEmpty()) {
                        actorsRecyclerView.visibility = View.GONE
                        actorsTextView.visibility = View.GONE
                    } else {
                        actorsRecyclerView.visibility = View.VISIBLE
                        actorsTextView.visibility = View.VISIBLE
                    }
                }
            }
        }
        viewLifecycleOwner.observe {
            viewModel.moviesSearchState.collectLatest {
                movieBigCardItemAdapter.submitData(it)
            }
        }
    }

    private fun checkEmptyMovieList(isListEmpty: Boolean) {
        with(binding) {
            if (isListEmpty) {
                noResultBigTextView.visibility = View.VISIBLE
                noResultSmallTextView.visibility = View.VISIBLE
                noResultImageView.visibility = View.VISIBLE
                moviesTextView.visibility = View.GONE
            } else {
                noResultBigTextView.visibility = View.GONE
                noResultSmallTextView.visibility = View.GONE
                noResultImageView.visibility = View.GONE
                moviesTextView.visibility = View.VISIBLE
            }
        }
    }
}
