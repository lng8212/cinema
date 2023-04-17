package com.longkd.cinema.person.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.longkd.cinema.ImagesConfigData
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.core.fragment.ToolbarConfiguration
import com.longkd.cinema.utils.lifecycle.observe
import com.longkd.cinema.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PersonFragment : BaseFragment(R.layout.fragment_person) {

    private val toolbarConfiguration = ToolbarConfiguration(
        startIconResId = R.drawable.ic_arrow_back,
        startIconClick = ::navBack
    )
    private val binding by viewBinding(com.longkd.cinema.databinding.FragmentPersonBinding::bind)
    private val viewModel by viewModels<PersonViewModel>()
    override val fragmentConfiguration = FragmentConfiguration()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIWithObservers()

    }

    private fun initUIWithObservers() {
        binding.customToolbar.configure(toolbarConfiguration)
        viewLifecycleOwner.observe {
            viewModel.personState.collectLatest { personState ->
                personState.personDetail?.let { person ->
                    with(binding) {
                        customToolbar.setTitle(person.name)
                        val posterUrl =
                            ImagesConfigData.secure_base_url + ImagesConfigData.poster_sizes?.get(3) + person.profile_path
                        Glide.with(requireContext())
                            .load(posterUrl)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .centerCrop().into(imgPerson)
                        txtName.text = person.name
                        txtDob.text = person.birthday
                        txtPob.text = person.place_of_birth
                        txtBio.text = person.biography
                    }
                }
                if (personState.isLoading) {
                    showProgressDialog()
                } else {
                    hideProgressDialog()
                }
            }
        }
    }
}