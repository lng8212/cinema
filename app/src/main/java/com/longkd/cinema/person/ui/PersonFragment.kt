package com.longkd.cinema.person.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.core.fragment.ToolbarConfiguration
import com.longkd.cinema.databinding.FragmentPersonBinding
import com.longkd.cinema.utils.lifecycle.observe
import com.longkd.cinema.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PersonFragment : BaseFragment(R.layout.fragment_person) {

    private val toolbarConfiguration = ToolbarConfiguration(
        titleResId = R.string.Cinema,
        startIconResId = R.drawable.ic_arrow_back,
        startIconClick = ::navBack
    )
    private val binding by viewBinding(FragmentPersonBinding::bind)
    private val viewModel by viewModels<PersonViewModel>()
    override val fragmentConfiguration = FragmentConfiguration(toolbarConfiguration)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIWithObservers()

    }

    private fun initUIWithObservers() {
        viewLifecycleOwner.observe {
            viewModel.personState.collectLatest { personState ->
                binding.txtTest.text = personState.personDetail?.biography
                if (personState.isLoading) {
                    showProgressDialog()
                } else {
                    hideProgressDialog()
                }
            }
        }
    }
}