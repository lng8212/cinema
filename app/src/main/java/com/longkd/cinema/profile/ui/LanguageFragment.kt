package com.longkd.cinema.profile.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.core.fragment.ToolbarConfiguration
import com.longkd.cinema.databinding.FragmentLanguageBinding
import com.longkd.cinema.profile.settings.LanguageListItem
import com.longkd.cinema.profile.settings.SelectionAdapter
import com.longkd.cinema.utils.setAppLocale
import com.longkd.cinema.utils.supportedLanguages
import com.longkd.cinema.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LanguageFragment : BaseFragment(R.layout.fragment_language) {

    private val toolbarConfiguration = ToolbarConfiguration(
        titleResId = R.string.language,
        startIconResId = R.drawable.ic_arrow_back,
        startIconClick = ::navBack
    )

    private val binding by viewBinding(FragmentLanguageBinding::bind)

    private val viewModel by viewModels<LanguageViewModel>()

    private val languageSelectionAdapter = SelectionAdapter(::onDifferentLanguageListItemClick)

    override val fragmentConfiguration = FragmentConfiguration(toolbarConfiguration)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.selectionRecyclerView.adapter = languageSelectionAdapter
        loadLanguages()
        binding.selectionRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
    }

    private fun loadLanguages() {
        val currentLanguage = viewModel.getCurrentLocale()
        languageSelectionAdapter.setItems(
            supportedLanguages.map {
                Locale(it).run {
                    LanguageListItem(
                        language,
                        getDisplayLanguage(this),
                        isSelected = currentLanguage == language
                    )
                }
            }
        )
    }

    private fun onDifferentLanguageListItemClick(languageListItem: LanguageListItem) {
        val langId = languageListItem.languageId
        context?.setAppLocale(langId)
        viewModel.setCurrentLocale(langId)
        restartActivity()
    }
}
