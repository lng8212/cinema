package com.longkd.cinema.profile.ui

import android.os.Bundle
import android.view.View
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.core.fragment.ToolbarConfiguration
import com.longkd.cinema.databinding.FragmentEditProfileBinding
import com.longkd.cinema.utils.viewbinding.viewBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BaseFragment(R.layout.fragment_edit_profile) {

    private val toolbarConfiguration = ToolbarConfiguration(
        titleResId = R.string.edit_profile,
        startIconClick = ::navBack,
        startIconResId = R.drawable.ic_arrow_back
    )
    override val fragmentConfiguration = FragmentConfiguration(toolbarConfiguration)

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    private val binding by viewBinding(FragmentEditProfileBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        hideBottomNavbar()
        with(binding) {
            if (!currentUser?.email.isNullOrEmpty()) {
                emailTextView.text = currentUser?.email
            } else {
                emailTextView.text = currentUser?.providerData?.get(1)?.email
            }
            nameTextView.text = currentUser?.displayName
            Glide.with(requireContext())
                .load(currentUser?.photoUrl)
                .placeholder(R.drawable.ic_person_24)
                .centerCrop()
                .into(personImageView)
        }
    }
}